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

        this.dayTitle = getResources().getStringArray(R.array.day_titles);

        this.dayText = new String[this.day.size()];

        this.dayText[0] = this.day.get("event");
//        Log.d("food_conains", "food_contains: " + this.day.get("food"));
        if (! this.day.get("food").isEmpty()) {
            this.dayText[1] = this.day.get("food") + " " + getResources().getString(R.string.day_food) + "\n" + getResources().getString(R.string.standard_foods);
        } else {
            this.dayText[1] = getResources().getString(R.string.standard_foods);
        }
        this.dayText[2] = this.day.get("openingTime");

        dayRecyclerView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        dayAdapter = new MyAdapter(dayTitle, dayText, dayIcons);
        dayRecyclerView.setAdapter(dayAdapter);
        dayLayoutManager = new LinearLayoutManager(getContext());
        dayRecyclerView.setLayoutManager(dayLayoutManager);

        return view;
    }
}
