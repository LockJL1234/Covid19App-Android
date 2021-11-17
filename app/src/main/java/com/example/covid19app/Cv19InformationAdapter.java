package com.example.covid19app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//adapter class for recycler view for Cv19Information class for generating card item
public class Cv19InformationAdapter extends RecyclerView.Adapter<Cv19InformationAdapter.Cv19InformationViewHolder>{

    public static final String CARD_POSITION = "Cv19InformationAdapter.cardPosition";
    private ArrayList<Cv19InfoHelperClass> cv19InfoHelperClasses;
    private Context context;

    //constructor for adapter class
    public Cv19InformationAdapter(ArrayList<Cv19InfoHelperClass> cv19InfoHelperClasses, Context context) {
        this.cv19InfoHelperClasses = cv19InfoHelperClasses;
        this.context = context;
    }

    //create viewholder for recyclerview row item
    @NonNull
    @Override
    public Cv19InformationAdapter.Cv19InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv19_info_row_item, parent, false);
        Cv19InformationAdapter.Cv19InformationViewHolder viewHolder = new Cv19InformationAdapter.Cv19InformationViewHolder(view);
        return viewHolder;
    }

    //set value for each component of row item
    @Override
    public void onBindViewHolder(@NonNull Cv19InformationAdapter.Cv19InformationViewHolder holder, int position) {
        Cv19InformationViewHolder viewHolder = (Cv19InformationViewHolder) holder;
        Cv19InfoHelperClass currentData = cv19InfoHelperClasses.get(position);
        viewHolder.infoImage.setImageResource(currentData.getInfoImg());
        viewHolder.title.setText(currentData.getInfo_title());
        viewHolder.desc.setText(currentData.getInfo_desc());
    }

    //get amount of item row to display
    @Override
    public int getItemCount() {
        return cv19InfoHelperClasses.size();
    }

    private void loadLearningContent(int pos){
        //intent to start activity to change to activity showing more detail of the chosen card (covid-19 information)
        Intent intent = new Intent(context,Cv19InfoDetails.class);
        //put Extra msg to allow Cv19InfoDetails determine which layout to inflate within the nested scroll view
        intent.putExtra(CARD_POSITION,pos);
        context.startActivity(intent);
    }

    //get each component in row item of recyclerview
    public class Cv19InformationViewHolder extends RecyclerView.ViewHolder {

        private TextView title, desc;
        private ImageView infoImage;
        private CardView cardView;

        public Cv19InformationViewHolder(@NonNull View itemView) {
            super(itemView);
            //find each component in cv19infodetail layout
            title = (TextView) itemView.findViewById(R.id.cv19_info_title);
            desc = (TextView) itemView.findViewById(R.id.cv19_info_desc);
            infoImage = (ImageView) itemView.findViewById(R.id.cv19_info_icon);
            cardView = itemView.findViewById(R.id.cv19_info_card);

            //set listener on card
            cardView.setOnClickListener( v ->{
                //each card clicked will return its card position
                //or so called ViewHolder's position / adapter position
                //Note: position starts from 0;direct compatible with array;
                int pos = getLayoutPosition();
                loadLearningContent(pos);
            });
        }
    }
}
