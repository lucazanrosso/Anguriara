package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.LinkedHashMap;

public class DayFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        Bundle args = this.getArguments();
        Calendar date = (Calendar) args.getSerializable("date");
        LinkedHashMap<String, Object> day = MainActivity.calendar.get(date);

        TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
        TextView eventText = (TextView) view.findViewById(R.id.event_text);
        ImageView eventImage = (ImageView) view.findViewById(R.id.event_image);
        String dayEvent = (String) day.get("event");
        int dayEventImage = (int) day.get("event_image");
        if (!dayEvent.isEmpty()) {
            eventTitle.setText(getResources().getString(R.string.event));
            eventText.setText(dayEvent + day.get("event_details"));
            eventImage.setImageResource(dayEventImage);
        } else {
            eventTitle.setText(getResources().getString(R.string.open));
            ((ViewGroup) eventText.getParent()).removeView(eventText);
        }

        TextView foodText = (TextView) view.findViewById(R.id.food_text);
        String dayFood = (String) day.get("food");
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            foodText.setText(getResources().getString(R.string.close));
        else if(date.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
            foodText.setText(getResources().getString(R.string.rustic_sandwich) + "\n" + getResources().getString(R.string.standard_foods));
        else if (!dayFood.isEmpty()) {
            if (date.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
                foodText.setText(dayFood + " " + getResources().getString(R.string.day_food));
            else
                foodText.setText(dayFood + " " + getResources().getString(R.string.day_food) + "\n" + getResources().getString(R.string.standard_foods));
        }
        else
            foodText.setText(getResources().getString(R.string.standard_foods));

        TextView openingTimeText = (TextView) view.findViewById(R.id.opening_time_text);
        String dayOpeningTime = (String) day.get("openingTime");
        if (!dayOpeningTime.isEmpty())
            openingTimeText.setText(dayOpeningTime);

        return view;
    }
}