package com.example.covid19app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

//Adapter class for recyclerview row item to duplicate card item with FirebaseRecyclerAdapter extension
public class LeaderboardAdapter extends FirebaseRecyclerAdapter<LeaderboardData, LeaderboardAdapter.LeaderboardViewHolder> {

    //arraylist LeaderboardData class
    private ArrayList<LeaderboardData> leaderboardData;

    //constructor for LeaderboardAdapter class with FirebaseRecyclerOptions(Firebase UI) parameter
    public LeaderboardAdapter(@NonNull FirebaseRecyclerOptions<LeaderboardData> options ){
        super(options);
    }

    //create viewholder for recyclerview row item
    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_row_item, parent, false);
        LeaderboardViewHolder viewHolder = new LeaderboardViewHolder(view);
        return viewHolder;
    }

    //set value for each component of row item
    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position, @NonNull LeaderboardData model) {
        holder.view1.setText(model.getUsername());
        holder.view2.setText(model.getScore());
    }

    //get each component in row item of recyclerview
    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        private TextView view1, view2;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            view1 = itemView.findViewById(R.id.leaderboard_name);
            view2 = itemView.findViewById(R.id.leaderboard_score);
        }
    }
}
