package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class DayFragment extends Fragment {

    View view;

    private int[] dayIcons = {R.drawable.ic_music_note_black_24dp,
            R.drawable.ic_local_pizza_black_24dp,
            R.drawable.ic_access_time_black_24dp};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);

        Bundle args = this.getArguments();
        GregorianCalendar date = (GregorianCalendar) args.getSerializable("date");
        LinkedHashMap<String, String> day = MainActivity.calendar.get(date);

        String[] dayTitle = new String[3];
        if (!day.get("event").isEmpty())
            dayTitle[0] = getResources().getString(R.string.event);
        else
            dayTitle[0] = getResources().getString(R.string.open);
        dayTitle[1] = getResources().getString(R.string.food);
        dayTitle[2] = getResources().getString(R.string.opening_time);

        String[] dayText = new String[3];
        dayText[0] = day.get("event") + " " + day.get("event_details");
        if (!day.get("food").isEmpty())
            dayText[1] = day.get("food") + " " + getResources().getString(R.string.day_food) + "\n" + getResources().getString(R.string.standard_foods);
        else
            dayText[1] = getResources().getString(R.string.standard_foods);
        if (!day.get("openingTime").isEmpty())
            dayText[2] = day.get("openingTime");
        else
            dayText[2] = getResources().getString(R.string.standard_opening_time);

        RecyclerView dayRecyclerView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        RecyclerView.Adapter dayAdapter = new DayAdapter(dayTitle, dayText, dayIcons);
        dayRecyclerView.setAdapter(dayAdapter);
        RecyclerView.LayoutManager dayLayoutManager = new LinearLayoutManager(getContext());
        dayRecyclerView.setLayoutManager(dayLayoutManager);

        return view;
    }
}