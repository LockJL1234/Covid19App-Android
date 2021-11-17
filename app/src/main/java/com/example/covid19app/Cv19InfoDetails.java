package com.example.covid19app;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Cv19InfoDetails extends AppCompatActivity {

    private ImageView cv19InfoImage;
    private Intent intent;
    private FloatingActionButton floatingActionButton;
    private String title;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv19_info_details_activity);
        cv19InfoImage = findViewById(R.id.cv19CollapseImage);

        //Intent Declaration to get position of selected card to display content that user chose
        intent = getIntent();
        int cardPosition = intent.getIntExtra(Cv19InformationAdapter.CARD_POSITION, 0);

        //Floating Action Button
        floatingActionButton = findViewById(R.id.scroll_fab);
        PopupMenu popMenu = new PopupMenu(Cv19InfoDetails.this, floatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            popMenu.show();
            popMenu.setOnMenuItemClickListener(item -> {
                callImplicitIntent(item.getGroupId(), item);
                return true;
            });
        });

        //create back button in toolbar to return to previous page (covid-19 information homepage)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //set content view of covid-19 information detail based on card item selected in previous activity to display selected information (covid-19 information homepage)
        switch (cardPosition) {
            //covid-19 information details content view
            case 0:
                getLayoutInflater().inflate(R.layout.cv19_info_details_about, findViewById(R.id.scroll_coLayout));
                popMenu.getMenuInflater().inflate(R.menu.cv19_detail_about_menu, popMenu.getMenu());
                cv19InfoImage.setImageResource(R.drawable.virus);
                title = "About Covid-19";
                break;
            //covid-19 variant information details content view
            case 1:
                getLayoutInflater().inflate(R.layout.cv19_info_details_variant, findViewById(R.id.scroll_coLayout));
                popMenu.getMenuInflater().inflate(R.menu.cv19_detail_variant_menu, popMenu.getMenu());
                cv19InfoImage.setImageResource(R.drawable.variants);
                title = "About Covid-19 Variant";
                break;
            //covid-19 transmission and prevention information details content view
            case 2:
                getLayoutInflater().inflate(R.layout.cv19_info_details_trans, findViewById(R.id.scroll_coLayout));
                popMenu.getMenuInflater().inflate(R.menu.cv19_detail_trans_menu, popMenu.getMenu());
                cv19InfoImage.setImageResource(R.drawable.transmission);
                title = "Covid-19 Transmission and Prevention";
                break;
            //covid-19 symptoms information details content view
            case 3:
                getLayoutInflater().inflate(R.layout.cv19_info_details_symptom, findViewById(R.id.scroll_coLayout));
                popMenu.getMenuInflater().inflate(R.menu.cv19_detail_symptoms_menu, popMenu.getMenu());
                cv19InfoImage.setImageResource(R.drawable.symptoms);
                title = "Covid-19 Symptoms";
                break;
            //covid-19 vaccine information details content view
            case 4:
                getLayoutInflater().inflate(R.layout.cv19_info_details_vaccine, findViewById(R.id.scroll_coLayout));
                popMenu.getMenuInflater().inflate(R.menu.cv19_detail_vaccine_menu, popMenu.getMenu());
                cv19InfoImage.setImageResource(R.drawable.vaccine_info);
                title = "Covid-19 Vaccination Information";
                break;
            //covid-19 vaccine side effect information details content view
            case 5:
                getLayoutInflater().inflate(R.layout.cv19_info_detail_vac_side, findViewById(R.id.scroll_coLayout));
                popMenu.getMenuInflater().inflate(R.menu.cv19_detail_side_menu, popMenu.getMenu());
                cv19InfoImage.setImageResource(R.drawable.side_effect);
                title = "Covid-19 Vaccination Side Effects";
                break;
            //error message
            default:
                Toast.makeText(this, "Error Loading Content", Toast.LENGTH_SHORT).show();
                title = "Error";
        }

        //create toolbar and collapsing toolbar with back button to return to previous page (covid-19 information homepage)
        toolbar = findViewById(R.id.cv19_info_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    //Load Implicit Intent based on selected item from menu of floating button
    public void callImplicitIntent(int groupId, MenuItem item) {
        final String QUERY = "query";
        final String WEBSITE = "website";
        final String MAP = "map";
        int itemId = item.getItemId();
        String searchItem, searchType;
        searchItem = searchType = "";

        //About Covid-19
        if (groupId == R.id.group_menu_about) {
            if (itemId == R.id.more_about_cv19) {
                searchItem = "covid-19";
                searchType = QUERY;
            } else {
                Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
            }
        }
        //About Covid-19 Variant
        else if (groupId == R.id.group_menu_variant) {
            if (itemId == R.id.more_about_variant) {
                searchItem = "covid-19 variant";
                searchType = QUERY;
            } else {
                Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
            }
        }
        //About Covid-19 Transmission
        else if (groupId == R.id.group_menu_trans) {
            if (itemId == R.id.more_about_trans) {
                searchItem = "covid-19 transmission";
                searchType = QUERY;
            } else if (itemId == R.id.more_about_prevent) {
                searchItem = "covid-19 prevention";
                searchType = QUERY;
            } else if (itemId == R.id.face_mask) {
                searchItem = "https://shopee.com.my/search?keyword=face%20mask";
                searchType = WEBSITE;
            } else {
                Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
            }
        }
        //About Covid-19 Symptoms
        else if (groupId == R.id.group_menu_symptoms) {
            if (itemId == R.id.more_about_symptoms) {
                searchItem = "covid-19 symptoms";
                searchType = QUERY;
            } else if (itemId == R.id.nearby_hospital) {
                searchItem = "nearby hospital";
                searchType = MAP;
            } else {
                Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
            }
        }
        //About Covid-19 Vaccine
        else if (groupId == R.id.group_menu_vaccine) {
            if (itemId == R.id.more_about_vaccine) {
                searchItem = "covid-19 vaccine";
                searchType = MAP;
            } else {
                Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
            }
        }
        //About Covid-19 Vaccine Side Effects
        else if (groupId == R.id.group_menu_side) {
            if (itemId == R.id.more_about_side) {
                searchItem = "covid-19 vaccine side effects";
                searchType = QUERY;
            } else if (itemId == R.id.nearby_hospital_side) {
                searchItem = "nearby hospital";
                searchType = MAP;
            } else {
                Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
            }
        }

        //Start loading Implicit Activity
        switch (searchType) {
            //perform query through google through default browser
            case QUERY:
                Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                searchIntent.putExtra(SearchManager.QUERY, searchItem);
                startActivity(searchIntent);
                break;
            //access to website through default browser
            case WEBSITE:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchItem));
                startActivity(browserIntent);
                break;
            //access google map to search for nearby hospital
            case MAP:
                Uri map = Uri.parse("geo:0,0?q=hospital");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, map);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }
    }
}
