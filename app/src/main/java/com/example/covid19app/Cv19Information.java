package com.example.covid19app;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Cv19Information extends AppCompatActivity {

    private ArrayList<Cv19InfoHelperClass> cv19InfoHelperClass;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Cv19InformationAdapter cv19InformationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv19_information_activity);

        //get recyclerview and set layout manager and adapter to create row of card item based on number of item in array
        recyclerView = findViewById(R.id.covid19_info_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cv19InfoHelperClass = new ArrayList<>();
        cv19InformationAdapter = new Cv19InformationAdapter(cv19InfoHelperClass, this);
        recyclerView.setAdapter(cv19InformationAdapter);
        //initialize covid-19 information item for recyclerview
        initializeCv19InfoItem();

        //create toolbar with back button to return to homepage
        toolbar = findViewById(R.id.cv19_info_toolbar);
        toolbar.setTitle("Covid-19 Information");
        setSupportActionBar(toolbar);
        //set 'back' navigation button on toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    //return to homepage after clicking back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Cv19Information.this, Homepage.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Initialization of contents for recycler view
    public void initializeCv19InfoItem() {
        //fetch the information from strings resource file
        String[] cv19InfoTitle = getResources().getStringArray(R.array.Covid19_information_title);
        String[] cv19DescDesc = getResources().getStringArray(R.array.Covid19_information_desc);
        //special array to fetch image from drawable resource according to array define in string resource file
        TypedArray cv19InfoImage = getResources().obtainTypedArray(R.array.Covid19_information_images);

        //clear the existing data; avoid duplication
        cv19InfoHelperClass.clear();

        //assign the information
        for (int i = 0; i < cv19InfoTitle.length; i++) {
            //add information helper class using constructor of helper class
            cv19InfoHelperClass.add(new Cv19InfoHelperClass(cv19InfoTitle[i], cv19DescDesc[i], cv19InfoImage.getResourceId(i, 0)));
        }
        //notify changes
        cv19InformationAdapter.notifyDataSetChanged();
        cv19InfoImage.recycle();
    }
}
