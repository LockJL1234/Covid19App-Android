package com.example.covid19app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Checklist extends AppCompatActivity {

    //define an arrayadapter for generating default checkbox in listview according to number of items(string array)
    private ListView listView;
    private ArrayAdapter<String> adapter;
    String[] arrayItemsToBring;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist_activity);

        listView = findViewById(R.id.checklist);
        //string array to get all items defined in string resource file
        arrayItemsToBring = getResources().getStringArray(R.array.Essential_Items);
        //instantiate adapter for listview to generate checkbox according to number of items in string array
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayItemsToBring);
        listView.setAdapter(adapter);

        //set up actionbar with back button and with clear all button
        toolbar = findViewById(R.id.checklist_toolbar);
        toolbar.setTitle("Items Checklist");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check if user selected all checkbox item every time when user checks a checkbox item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listView.getCheckedItemCount() == arrayItemsToBring.length)
                    Toast.makeText(Checklist.this, "You Have Successfully Prepared All Essential Item.", Toast.LENGTH_LONG).show();
            }
        });
    }

    //create clear all icon on the other end of toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checklist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //returns user to homepage when back button is clicked
            case android.R.id.home:
                startActivity(new Intent(Checklist.this, Homepage.class));
                return true;
            //clear all checked item in listview checkbox when clear all icon is clicked
            case R.id.checklist_clear:
                for (int i = 0; i < listView.getAdapter().getCount(); i++)
                    listView.setItemChecked(i, false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
