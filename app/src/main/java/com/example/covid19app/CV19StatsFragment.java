package com.example.covid19app;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//fragment java class
public class CV19StatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private String date_source;
    private String[] cv19_desc_total, cv19_desc_daily;
    private ArrayList<String> cv19_total_num = new ArrayList<>(), cv19_daily_num = new ArrayList<>();
    private ArrayList<Covid19Data> covid19Data = new ArrayList<>();
    private Covid19Adapter covid19Adapter;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private Cv19Data cv19Data;
    private Document doc;
    private Elements aCases, recovery_death, vaccination, dCases, data_date;
    private TextView dateView;

    //generate recycler view for homepage fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cv19stats, container, false);
        progressBar = view.findViewById(R.id.cv19_progressBar);
        recyclerView = view.findViewById(R.id.covid19_stat_recycler);
        dateView = view.findViewById(R.id.date_source);

        cv19Data = new Cv19Data();
        cv19Data.execute();
        return view;
    }

    //asyntask java class for web scrapping https://covidnow.moh.gov.my/ for Covid-19 statistics
    private class Cv19Data extends AsyncTask<Void, Void, Void> {

        //task to execute before starting AsyncTask; change progress bar to visible and start loading animation
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        }

        //task to execute after background task
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            dateView.setText(date_source);

            //add Covid-19 statistics to ArrayList object of java class Covid19Data to get data to display row item in recyclervew
            covid19Data = new ArrayList<>();
            cv19_desc_total = getResources().getStringArray(R.array.Covid19_Desc_Total);
            cv19_desc_daily = getResources().getStringArray(R.array.Covid19_Desc_Daily);
            covid19Data.add(new Covid19Data(R.drawable.disease, cv19_desc_total[0], cv19_total_num.get(0), cv19_desc_daily[0], cv19_daily_num.get(0)));
            covid19Data.add(new Covid19Data(R.drawable.death, cv19_desc_total[1], cv19_total_num.get(1), cv19_desc_daily[1], cv19_daily_num.get(1)));
            covid19Data.add(new Covid19Data(R.drawable.recovery, cv19_desc_total[2], cv19_total_num.get(2), cv19_desc_daily[2], cv19_daily_num.get(2)));
            covid19Data.add(new Covid19Data(R.drawable.vaccine, cv19_desc_total[3], cv19_total_num.get(3), cv19_desc_daily[3], cv19_daily_num.get(3)));
            covid19Data.add(new Covid19Data(R.drawable.disease, cv19_desc_total[4], cv19_total_num.get(4), cv19_desc_daily[4], cv19_daily_num.get(4)));

            //generate recycler view with row items of Covid-19 statistics
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            covid19Adapter = new Covid19Adapter(getContext(), covid19Data);
            recyclerView.setAdapter(covid19Adapter);

            //stop progress bar loading animation and make progress bar disappear; notify recyclerview adapter on data changed.
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            covid19Adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        //task to execute in background
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //web scrap to parse content of webpage
                doc = Jsoup.connect("https://covidnow.moh.gov.my/").get();

                //retrieve element tag content based on entered class name
                aCases = doc.getElementsByClass("bg-blue-100 px-2 m-auto");
                recovery_death = doc.getElementsByClass("number flex justify-center gap-1.5");
                vaccination = doc.getElementsByClass("relative self-end");
                dCases = doc.getElementsByClass("font-bold text-xl lg:text-2xl mr-auto");
                data_date = doc.getElementsByClass("col-span-1 text-xs text-gray-500 text-right tracking-tighter leading-3");

                //get date of data source
                date_source = data_date.get(0).text().trim();

                //get text from retrieve element tag content and store into arrayList
                ArrayList<String> temp = new ArrayList<>();
                temp.add(dCases.get(5).text());
                temp.add(dCases.get(4).text());
                temp.addAll(Arrays.asList(recovery_death.get(3).text().replace(
                        "Deaths due to COVID - this differs from deaths with COVID (positive at time of death) but with non-COVID causes of death ", "")
                        .split(" ", 2)));
                temp.addAll(Arrays.asList(recovery_death.get(2).text().split(" ", 2)));
                temp.add(vaccination.get(1).text());
                temp.add(vaccination.get(0).text());
                temp.addAll(Arrays.asList(aCases.get(0).text().split(" ", 2)));
                for(int i = 0; i < 10; i+=2 ){
                    cv19_total_num.add(temp.get(i).toString());
                    cv19_daily_num.add(temp.get(i+1).toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}