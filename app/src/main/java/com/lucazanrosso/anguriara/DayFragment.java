package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Map;

public class DayFragment extends Fragment{

    View view;
    private Calendar date;
    private Map<String, String> day;

    private String[] dayTitle;
    private String[] dayText;
    private int[] dayIcons = {R.drawable.ic_music_note_black_24dp,
            R.drawable.ic_local_pizza_black_24dp,
            R.drawable.ic_access_time_black_24dp};

    private RecyclerView dayRecyclerView;
    private RecyclerView.Adapter dayAdapter;
    private RecyclerView.LayoutManager dayLayoutManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);
        Bundle args = this.getArguments();
        this.date = (Calendar) args.getSerializable("date");
        this.day = (Map<String, String>) args.getSerializable("day");

        this.dayTitle = new String[3];
        if (! this.day.get("event").isEmpty())
            this.dayTitle[0] = getResources().getString(R.string.event);
        else
            this.dayTitle[0] = getResources().getString(R.string.open);
        this.dayTitle[1] = getResources().getString(R.string.food);
        this.dayTitle[2] = getResources().getString(R.string.opening_time);

        this.dayText = new String[3];
        this.dayText[0] = this.day.get("event") + " " + this.day.get("event_details");
        if (! this.day.get("food").isEmpty())
            this.dayText[1] = this.day.get("food") + " " + getResources().getString(R.string.day_food) + "\n" + getResources().getString(R.string.standard_foods);
        else
            this.dayText[1] = getResources().getString(R.string.standard_foods);
        if (! this.day.get("openingTime").isEmpty())
            this.dayText[2] = this.day.get("openingTime");
        else
            this.dayText[2] = getResources().getString(R.string.standard_opening_time);

        dayRecyclerView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        dayAdapter = new MyAdapter(dayTitle, dayText, dayIcons);
        dayRecyclerView.setAdapter(dayAdapter);
        dayLayoutManager = new LinearLayoutManager(getContext());
        dayRecyclerView.setLayoutManager(dayLayoutManager);

        return view;
    }
}
