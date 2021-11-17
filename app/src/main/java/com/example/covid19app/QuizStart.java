package com.example.covid19app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuizStart extends AppCompatActivity {

    private Toolbar toolbar;
    private Button quizStartBtn;
    private String userID, username = "";
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_start_activity);

        quizStartBtn = findViewById(R.id.quiz_start_btn);

        //get user id of current user to retrieve username stored in realtime database
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        readData(userID);

        //create toolbar with back button to return to homepage
        toolbar = findViewById(R.id.quiz_toolbar);
        toolbar.setTitle("Quiz");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //start quiz after clicking start button and pass username data retrieved from database with current user's id to quiz activity
        quizStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizStart.this, Quiz.class).putExtra("Username", username));
            }
        });
    }

    //return to homepage after clicking back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(QuizStart.this, Homepage.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //retrieve username from database based on current user's userID
    private void readData(String userID) {
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //get data from database
                DataSnapshot dataSnapshot = task.getResult();
                username = String.valueOf(dataSnapshot.child("username").getValue());
            }
        });
    }
}
