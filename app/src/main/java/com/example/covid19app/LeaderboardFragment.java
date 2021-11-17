package com.example.covid19app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private LeaderboardAdapter leaderboardAdapter;
    private DatabaseReference db;
    private ProgressBar progressBar;

    //default constructor for Leaderboard Fragment
    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        progressBar = view.findViewById(R.id.leaderboard_progressBar);
        recyclerView = view.findViewById(R.id.leaderboard_recycler);

        recyclerView.setHasFixedSize(true);
        //set vertical linear layout for recyclerview with reverse layout
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
        //recyclerview display item from bottom to top
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        db = FirebaseDatabase.getInstance().getReference();

        //firebase Ui for quick connection to common UI elements of firebase API
        //FirebaseRecyclerOptions to create query to retrieve leaderboard data from realtime database and sort by "score" value in ascending order
        //instantiate FirebaseRecyclerOptions with LeaderboardData class
        FirebaseRecyclerOptions<LeaderboardData> options =new FirebaseRecyclerOptions.Builder<LeaderboardData>()
                        .setQuery(db.child("leaderboard").orderByChild("score"), LeaderboardData.class)
                        .build();

        //instantiate Leaderboard recyclerview adapter class with FirebaseRecyclerOptions query
        leaderboardAdapter = new LeaderboardAdapter(options);
        recyclerView.setAdapter(leaderboardAdapter);

        //if data updated, stop loading progress and set progress bar as invisible.
        db.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ScoreBoardActivity", "Failed to read fire.data");
            }
        });

        return view;
    }

    //adapter of leaderboard recyclerview listen for data changes during onStart() lifecycle of fragment
    @Override
    public void onStart() {
        super.onStart();
        leaderboardAdapter.startListening();
    }

    //adapter of leaderboard recyclerview stop listening for data changes during onStop() lifecycle of fragment
    @Override
    public void onStop() {
        super.onStop();
        leaderboardAdapter.stopListening();
    }
}