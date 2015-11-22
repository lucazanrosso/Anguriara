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
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class DayFragment extends Fragment{

    View view;
    private LinkedHashMap<String, String> day;
    private GregorianCalendar date;

    private String[] daysOfWeek;
    private String[] months;

    private String[] dayTitle;
    private String[] dayText;
    private int[] dayIcons = {R.drawable.ic_music_note_black_24dp,
            R.drawable.ic_local_pizza_black_24dp,
            R.drawable.ic_access_time_black_24dp};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);

        Bundle args = this.getArguments();
        this.date = (GregorianCalendar) args.getSerializable("date");
        this.day = (LinkedHashMap<String, String>) args.getSerializable("day");

        this.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        this.months = getResources().getStringArray(R.array.months);

        String thisDayOfWeek = daysOfWeek[date.get(Calendar.DAY_OF_WEEK) - 1];
        int thisDayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        String thisMonth = months[date.get(Calendar.MONTH)];
        MainActivity.toolbar.setTitle(thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth);

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

        RecyclerView dayRecyclerView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        RecyclerView.Adapter dayAdapter = new DayAdapter(dayTitle, dayText, dayIcons);
        dayRecyclerView.setAdapter(dayAdapter);
        RecyclerView.LayoutManager dayLayoutManager = new LinearLayoutManager(getContext());
        dayRecyclerView.setLayoutManager(dayLayoutManager);

        return view;
    }
}
