package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class DayFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        Bundle args = this.getArguments();
        GregorianCalendar date = (GregorianCalendar) args.getSerializable("date");
        boolean isWednesday = args.getBoolean("isWednesday");
        boolean isSunday = args.getBoolean("isSunday");
        LinkedHashMap<String, String> day = MainActivity.calendar.get(date);

        TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
        TextView eventText = (TextView) view.findViewById(R.id.event_text);
        if (!day.get("event").isEmpty()) {
            eventTitle.setText(getResources().getString(R.string.event));
            eventText.setText(day.get("event") + day.get("event_details"));
        } else {
            eventTitle.setText(getResources().getString(R.string.open));
            ((ViewGroup) eventText.getParent()).removeView(eventText);
        }

        TextView foodText = (TextView) view.findViewById(R.id.food_text);
        if (isSunday)
            foodText.setText(getResources().getString(R.string.close));
        else if (!day.get("food").isEmpty()) {
            if (isWednesday)
                foodText.setText(day.get("food") + " " + getResources().getString(R.string.day_food));
            else
                foodText.setText(day.get("food") + " " + getResources().getString(R.string.day_food) + "\n" + getResources().getString(R.string.standard_foods));
        }
        else
            foodText.setText(getResources().getString(R.string.standard_foods));

        TextView openingTimeText = (TextView) view.findViewById(R.id.opening_time_text);
        if (!day.get("openingTime").isEmpty())
            openingTimeText.setText(day.get("openingTime"));

        return view;
    }
}