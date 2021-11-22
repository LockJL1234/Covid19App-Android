package com.example.covid19app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class UpdateProfile extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private Toolbar toolbar;
    private TextInputEditText usernameText, emailText, ageText, schoolText;
    private CircularProgressButton updateProfileBtn;
    private String uname, email, age, school, userID;
    private DatabaseReference reference;
    private FirebaseDatabase rootNode;
    private ProfileHelperClass helperClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_activity);

        fAuth = FirebaseAuth.getInstance();
        updateProfileBtn = (CircularProgressButton) findViewById(R.id.update_btn);
        usernameText = findViewById(R.id.new_username);
        emailText = findViewById(R.id.new_email);
        ageText = findViewById(R.id.new_age);
        schoolText = findViewById(R.id.new_school);

        //get instance of user that is currently logged into app and get user id for retrieving user data from realtime database
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //set up actionbar with back button
        toolbar = findViewById(R.id.update_profile_toolbar);
        toolbar.setTitle("Update Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //retrieve user data from realtime database based on user id and input into text field
        reference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //get data from database
                DataSnapshot dataSnapshot = task.getResult();
                uname = String.valueOf(dataSnapshot.child("username").getValue());
                email = String.valueOf(dataSnapshot.child("email").getValue());
                age = String.valueOf(dataSnapshot.child("age").getValue());
                school = String.valueOf(dataSnapshot.child("school").getValue());
                usernameText.setText(uname);
                emailText.setText(email);
                ageText.setText(age);
                schoolText.setText(school);
            }
        });

        //update user data in realtime database after clicking update button
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileBtn.startAnimation();
                uname = usernameText.getText().toString();
                email = emailText.getText().toString();
                age = ageText.getText().toString();
                school = schoolText.getText().toString();
                updateProfile();
            }
        });
    }

    //function to update user data after confirming updating action and return to profile page
    private void updateProfile() {
        new AlertDialog.Builder(UpdateProfile.this)
                .setTitle("Updating Profile")
                .setMessage("Confirm information entered for updating profile?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //profile helperclass to get data for storing into realtime database
                        helperClass = new ProfileHelperClass(uname, school, email, age);
                        reference.child(userID).setValue(helperClass);
                        Toast.makeText(UpdateProfile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        updateProfileBtn.revertAnimation();
                    }
                }).show();
    }

    //return to previous page (user profile page)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
