package com.lucazanrosso.anguriara;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class CalendarFragment extends Fragment {

    View view;

    //TO IMPROVE
    private int monthSelected = -1;

    private Calendar today = new GregorianCalendar(2015, 5, 5);
    private String[] daysOfWeek;
    private String[] months;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.calendar));

        this.view = inflater.inflate(R.layout.fragment_calendar, container, false);

        this.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        this.months = getResources().getStringArray(R.array.months);

        setThisDay();

        final ImageButton juneButton = (ImageButton) view.findViewById(R.id.june_button);
        final ImageButton julyButton = (ImageButton) view.findViewById(R.id.july_button);
        juneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthCalendar(Calendar.JUNE);
                juneButton.setVisibility(View.INVISIBLE);
                julyButton.setVisibility(View.VISIBLE);
                monthSelected = 5;
            }
        });
        julyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthCalendar(Calendar.JULY);
                julyButton.setVisibility(View.INVISIBLE);
                juneButton.setVisibility(View.VISIBLE);
                monthSelected = 6;
            }
        });

        //TO IMPROVE
        if (monthSelected != -1) {
            if (monthSelected == 5) {
                setMonthCalendar(Calendar.JUNE);
                juneButton.setVisibility(View.INVISIBLE);
                julyButton.setVisibility(View.VISIBLE);
                monthSelected = 5;
            } else if (monthSelected == 6) {
                setMonthCalendar(Calendar.JULY);
                julyButton.setVisibility(View.INVISIBLE);
                juneButton.setVisibility(View.VISIBLE);
                monthSelected = 6;
            }
        } else {
            if (this.today.get(Calendar.MONTH) < 6) {
                setMonthCalendar(Calendar.JUNE);
                juneButton.setVisibility(View.INVISIBLE);
                julyButton.setVisibility(View.VISIBLE);
                monthSelected = 5;
            } else {
                setMonthCalendar(Calendar.JULY);
                julyButton.setVisibility(View.INVISIBLE);
                juneButton.setVisibility(View.VISIBLE);
                monthSelected = 6;
            }
        }

        return this.view;
    }

    private void setThisDay () {
        ImageView imageView = (ImageView) this.view.findViewById(R.id.card_view_image);
        TextView titleTextView = (TextView) this.view.findViewById(R.id.card_view_title);
        TextView subTitleTextView = (TextView) this.view.findViewById(R.id.card_view_sub_title);
        CardView thisDayCardView = (CardView) this.view.findViewById(R.id.card_view);

        String thisDayOfWeek = daysOfWeek[this.today.get(Calendar.DAY_OF_WEEK) - 1];
        int thisDayOfMonth = this.today.get(Calendar.DAY_OF_MONTH);
        String thisMonth = months[this.today.get(Calendar.MONTH)];
        String cardViewTitle = thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth;
        titleTextView.setText(cardViewTitle);

        if (MainActivity.calendar.containsKey(this.today)) {
            imageView.setImageResource(R.drawable.open);
            String dayEventAndFood = getResources().getString(R.string.event) + ": " + MainActivity.calendar.get(this.today).get("event") + "\n";
            if (! MainActivity.calendar.get(this.today).get("food").isEmpty())
                dayEventAndFood += getResources().getString(R.string.food) + ": " + MainActivity.calendar.get(this.today).get("food");
            subTitleTextView.setText(dayEventAndFood);

            final Bundle dayArgs = new Bundle();
            dayArgs.putSerializable("date", this.today);
            thisDayCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
                    dayScreenSlidePagerFragment.setArguments(dayArgs);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, dayScreenSlidePagerFragment);
                    transaction.addToBackStack("secondary");
                    transaction.commit();
                }
            });
        } else {
            imageView.setImageResource(R.drawable.close);
            subTitleTextView.setText(getResources().getString(R.string.close));
        }
    }

    private void setMonthCalendar(int month) {
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> monthCalendar = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
            if (entry.getKey().get(Calendar.MONTH) == month) {
                monthCalendar.put(entry.getKey(), entry.getValue());
            }
        }

        Calendar date = new GregorianCalendar(2015, month, 1);

        LinearLayout monthLayout = (LinearLayout) view.findViewById(R.id.month_layout);
        monthLayout.removeAllViews();
        LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams dayLayoutParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView textView = (TextView) view.findViewById(R.id.month);

        Iterator iterator = monthCalendar.entrySet().iterator();
        if (iterator.hasNext()) {
            LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry = (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>>) iterator.next();

            textView.setText(months[month]);
            while (month == date.get(Calendar.MONTH)) {
                LinearLayout weekLinearLayout = new LinearLayout(getActivity());
                weekLinearLayout.setLayoutParams(weekLayoutParams);
                for (int j = 0; j <= 6; j++) {
                    if (((date.get(Calendar.DAY_OF_WEEK) + 5) % 7) == j && date.get(Calendar.MONTH) == month) {
                        Button button = new Button(getActivity());
                        button.setLayoutParams(dayLayoutParams);
                        button.setBackgroundResource(0);
                        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        button.setText(Integer.toString(date.get(Calendar.DAY_OF_MONTH)));
                        if (entry.getKey().get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)) {
                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
                            button.setTypeface(null, Typeface.BOLD);

                            final Bundle dayArgs = new Bundle();
                            dayArgs.putSerializable("date", entry.getKey());
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
                                    dayScreenSlidePagerFragment.setArguments(dayArgs);
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_container, dayScreenSlidePagerFragment);
                                    transaction.addToBackStack("secondary");
                                    transaction.commit();
                                }
                            });
                            if (iterator.hasNext())
                                entry = (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>>) iterator.next();
                        } else {
                            button.setEnabled(false);
                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.disabled_text));
                        }
                        weekLinearLayout.addView(button);
                        date.add(Calendar.DAY_OF_YEAR, 1);
                    } else {
                        View voidView = new View(getActivity());
                        voidView.setLayoutParams(dayLayoutParams);
                        weekLinearLayout.addView(voidView);
                    }
                }
                monthLayout.addView(weekLinearLayout);
            }
        }
    }
}