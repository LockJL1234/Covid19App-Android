package com.example.covid19app;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private FrameLayout frameLayout;
    private ImageButton profileRefresh_btn;
    private View view;
    private TextView unameView, schoolView, emailView, ageView;
    private String userID;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        unameView = view.findViewById(R.id.username_profile);
        schoolView = view.findViewById(R.id.school_profile);
        emailView = view.findViewById(R.id.email_profile);
        ageView = view.findViewById(R.id.age_profile);
        profileRefresh_btn = view.findViewById(R.id.profile_refresh);

        //retrieve user profile data from realtime database
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserProfile();

        //frame layout that is clickable that direct user to update user profile page
        frameLayout = view.findViewById(R.id.update_profile);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpdateProfile.class));
            }
        });

        //refresh button to update user profile with latest data
        profileRefresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserProfile();
            }
        });
        return view;
    }

    //retrieve user data from realtime database according to user id
    private void getUserProfile(){
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //get data user from realtime database
                DataSnapshot dataSnapshot = task.getResult();
                unameView.setText(String.valueOf(dataSnapshot.child("username").getValue()));
                emailView.setText(String.valueOf(dataSnapshot.child("email").getValue()));
                ageView.setText(String.valueOf(dataSnapshot.child("age").getValue()));
                schoolView.setText(String.valueOf(dataSnapshot.child("school").getValue()));
            }
        });
    }

}