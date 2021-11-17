package com.example.covid19app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//Adapter class for recyclerview row item to duplicate card item
public class Covid19Adapter extends RecyclerView.Adapter<Covid19Adapter.Covid19ViewHolder> {

    private ArrayList<Covid19Data> covid19Data;
    private Context context;

    //constructor for adapter class
    public Covid19Adapter(Context context, ArrayList<Covid19Data> covid19Data) {
        this.context = context;
        this.covid19Data = covid19Data;
    }

    //create viewholder for recyclerview row item
    @NonNull
    @Override
    public Covid19ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.covid19_row_item, parent, false);
        Covid19ViewHolder viewHolder = new Covid19ViewHolder(view);
        return viewHolder;
    }

    //set value for each component of row item
    @Override
    public void onBindViewHolder(@NonNull Covid19ViewHolder holder, int position) {
        Covid19ViewHolder viewHolder = (Covid19ViewHolder) holder;
        Covid19Data currentData = covid19Data.get(position);
        viewHolder.imageView.setImageResource(currentData.getImageResource());
        viewHolder.view1.setText(currentData.getCv19_desc_total());
        viewHolder.view2.setText(currentData.getCv19_total_num());
        viewHolder.view3.setText(currentData.getCv19_desc_daily());
        viewHolder.view4.setText(currentData.getCv19_daily_num());
    }

    //get amount of item row to display
    @Override
    public int getItemCount() {
        return covid19Data.size();
    }

    //get each component in row item of recyclerview
    public class Covid19ViewHolder extends RecyclerView.ViewHolder {

        private TextView view1, view2, view3, view4;
        private ImageView imageView;

        public Covid19ViewHolder(@NonNull View itemView) {
            super(itemView);
            view1 = (TextView) itemView.findViewById(R.id.cv19_describe1);
            view2 = itemView.findViewById(R.id.cv19_stats1);
            view3 = itemView.findViewById(R.id.cv19_describe2);
            view4 = itemView.findViewById(R.id.cv19_stats2);
            imageView = (ImageView) itemView.findViewById(R.id.cv19_icon);
        }
    }
}
