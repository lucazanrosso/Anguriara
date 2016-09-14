package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.anguriara));

        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        getThisDay();

        return view;
    }

    public void getThisDay() {
        TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
        ImageView eventImage = (ImageView) view.findViewById(R.id.event_image);
        TextView foodTitle = (TextView) view.findViewById(R.id.food_title);
        ImageView foodImage = (ImageView) view.findViewById(R.id.food_image);

        if (MainActivity.calendar.containsKey(MainActivity.today)) {
            if(MainActivity.today.equals(MainActivity.badDay)) {
                eventTitle.setText(getResources().getString(R.string.bad_weather));
            } else {
                eventTitle.setText((String) MainActivity.calendar.get(MainActivity.today).get("event"));
                eventImage.setImageResource((int) MainActivity.calendar.get(MainActivity.today).get("event_image"));
                String dayFood = (String) MainActivity.calendar.get(MainActivity.today).get("food");
                if (dayFood.isEmpty()) {
                    foodTitle.setVisibility(View.GONE);
                    foodImage.setVisibility(View.GONE);
                } else {
                    foodTitle.setText(dayFood);
                    int dayImageId = (int) MainActivity.calendar.get(MainActivity.today).get("food_image");
                    if (dayImageId == 0) {
                        CardView foodCard = (CardView) view.findViewById(R.id.food_card);
                        foodCard.setVisibility(View.GONE);
                    }
                    else
                        foodImage.setImageResource(dayImageId);
                }
                Button detailsButton = (Button) view.findViewById(R.id.details_button);
                Button scheduleButton = (Button) view.findViewById(R.id.schedule_button);
                final Bundle dayArgs = new Bundle();
                dayArgs.putSerializable("date", MainActivity.today);
                detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
                        dayScreenSlidePagerFragment.setArguments(dayArgs);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container, dayScreenSlidePagerFragment)
                                .addToBackStack("secondary")
                                .commit();
                    }
                });
                scheduleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container, new CalendarFragment())
                                .addToBackStack("secondary")
                                .commit();
                    }
                });
            }
        } else {
            eventTitle.setText(getResources().getString(R.string.close));
            eventImage.setVisibility(View.GONE);
            foodTitle.setVisibility(View.GONE);
            foodImage.setVisibility(View.GONE);
        }
    }
}