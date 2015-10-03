package com.luca.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Map;

public class DayFragment extends Fragment{

    View view;
    private Calendar date;
    private Map<String, String> day;

    private String[] dayString;
    private int[] dayIcons = {R.drawable.ic_music_note_black_24dp,
            R.drawable.ic_local_pizza_black_24dp};

    private RecyclerView dayRecyclerView;
    private RecyclerView.Adapter dayAdapter;
    private RecyclerView.LayoutManager dayLayoutManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);
        Bundle args = this.getArguments();
        this.date = (Calendar) args.getSerializable("date");
        this.day = (Map<String, String>) args.getSerializable("day");

        this.dayString = new String[this.day.size()];

        this.dayString[0] = getResources().getString(R.string.event) + this.day.get("event");
        this.dayString[1] = getResources().getString(R.string.food) + "\n" + this.day.get("food");

//        Log.d("day", "day: " + dayString[0] + dayString[1]);

        dayRecyclerView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        dayAdapter = new MyAdapter(dayString, dayIcons);
        dayRecyclerView.setAdapter(dayAdapter);

        dayLayoutManager = new LinearLayoutManager(getContext());
        dayRecyclerView.setLayoutManager(dayLayoutManager);


        return view;
    }
}
