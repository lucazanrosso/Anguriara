package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class DayFragment extends Fragment{

    View view;
    private LinkedHashMap<String, String> day;

    private String[] dayTitle;
    private String[] dayText;
    private int[] dayIcons = {R.drawable.ic_music_note_black_24dp,
            R.drawable.ic_local_pizza_black_24dp,
            R.drawable.ic_access_time_black_24dp};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);

//        if (savedInstanceState != null) {
//            this.day = (LinkedHashMap<String, String>) savedInstanceState.getSerializable("day");
//            this.dayTitle = savedInstanceState.getStringArray("dayTitle");
//            this.dayText = savedInstanceState.getStringArray("dayText");
//            this.dayIcons = savedInstanceState.getIntArray("dayIcons");
//            Log.d("prova", "0");
//        } else {
            Bundle args = this.getArguments();
            this.day = (LinkedHashMap<String, String>) args.getSerializable("day");

            this.dayTitle = new String[3];
            if (!this.day.get("event").isEmpty())
                this.dayTitle[0] = getResources().getString(R.string.event);
            else
                this.dayTitle[0] = getResources().getString(R.string.open);
            this.dayTitle[1] = getResources().getString(R.string.food);
            this.dayTitle[2] = getResources().getString(R.string.opening_time);

            this.dayText = new String[3];
            this.dayText[0] = this.day.get("event") + " " + this.day.get("event_details");
            if (!this.day.get("food").isEmpty())
                this.dayText[1] = this.day.get("food") + " " + getResources().getString(R.string.day_food) + "\n" + getResources().getString(R.string.standard_foods);
            else
                this.dayText[1] = getResources().getString(R.string.standard_foods);
            if (!this.day.get("openingTime").isEmpty())
                this.dayText[2] = this.day.get("openingTime");
            else
                this.dayText[2] = getResources().getString(R.string.standard_opening_time);
            Log.d("prova", "1");
//        }

        RecyclerView dayRecyclerView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        RecyclerView.Adapter dayAdapter = new MyAdapter(dayTitle, dayText, dayIcons);
        dayRecyclerView.setAdapter(dayAdapter);
        RecyclerView.LayoutManager dayLayoutManager = new LinearLayoutManager(getContext());
        dayRecyclerView.setLayoutManager(dayLayoutManager);

        return view;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("day", day);
//        outState.putStringArray("dayTitle", dayTitle);
//        outState.putStringArray("dayText", dayText);
//        outState.putIntArray("dayIcons", dayIcons);
//    }
}
