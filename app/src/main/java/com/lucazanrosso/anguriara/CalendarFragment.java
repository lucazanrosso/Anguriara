package com.lucazanrosso.anguriara;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CalendarFragment extends Fragment {

    View view;

    private Calendar today = new GregorianCalendar(2015, 5, 5);
    private String[] daysOfWeek;
    private String[] months;

    private String cardViewTitle;

    private Calendar date;
    private String toolbarTitle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_calendar, container, false);

        LinearLayout calendarLayout = (LinearLayout) this.view.findViewById(R.id.calendar_layout);

        MainActivity.toolbar.setTitle(getResources().getString(R.string.calendar));

        this.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        this.months = getResources().getStringArray(R.array.months);

        thisDay();
        calendarLayout.addView(setMonthCalendar(Calendar.JUNE));
        calendarLayout.addView(setMonthCalendar(Calendar.JULY));

        return this.view;
    }

    public void thisDay () {
        ImageView imageView = (ImageView) this.view.findViewById(R.id.card_view_image);
        TextView titleTextView = (TextView) this.view.findViewById(R.id.card_view_title);
        TextView subTitleTextView = (TextView) this.view.findViewById(R.id.card_view_sub_title);
        Button button = (Button) this.view.findViewById(R.id.card_view_button);

        String thisDayOfWeek = daysOfWeek[today.get(Calendar.DAY_OF_WEEK) - 1];
        int thisDayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        String thisMonth = months[today.get(Calendar.MONTH)];
        this.cardViewTitle = thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth;
        titleTextView.setText(this.cardViewTitle);

        if (MainActivity.calendar.containsKey(this.today)) {
            imageView.setImageResource(R.drawable.open);
            String dayEventAndFood = getResources().getString(R.string.event) + ": " +
                    MainActivity.calendar.get(today).get("event") + "\n" +
                    getResources().getString(R.string.food) + ": " +
                    MainActivity.calendar.get(today).get("food");
            subTitleTextView.setText(dayEventAndFood);

            final Bundle dayArgs = new Bundle();
            dayArgs.putSerializable("date", today);
            dayArgs.putSerializable("day", MainActivity.calendar.get(today));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.toolbar.setTitle(cardViewTitle);
                    DayFragment dayFragment = new DayFragment();
                    dayFragment.setArguments(dayArgs);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, dayFragment);
                    transaction.addToBackStack("secondary");
                    transaction.commit();
                }
            });
            button.setVisibility(View.VISIBLE);
        } else {
            imageView.setImageResource(R.drawable.close);
            subTitleTextView.setText(getResources().getString(R.string.close));
            button.setVisibility(View.GONE);
        }

    }

    public View setMonthCalendar(int month) {
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> monthCalendar = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
            if (entry.getKey().get(Calendar.MONTH) == month) {
                monthCalendar.put(entry.getKey(), entry.getValue());
            }
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View monthView = inflater.inflate(R.layout.fragment_month, null, false);
        this.date = new GregorianCalendar(2015, month, 1);

        LinearLayout calendarLinearLayout = (LinearLayout) monthView.findViewById(R.id.month_layout);
        LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams dayLayoutParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView textView = (TextView) monthView.findViewById(R.id.month);

        Iterator iterator = monthCalendar.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<GregorianCalendar, Map<String, String>> entry = (Map.Entry<GregorianCalendar, Map<String, String>>) iterator.next();

        textView.setText(months[month]);
            while (month == this.date.get(Calendar.MONTH)) {
                LinearLayout weekLinearLayout = new LinearLayout(getActivity());
                weekLinearLayout.setLayoutParams(weekLayoutParams);
                for (int j = 0; j <= 6; j++) {
                    if (((this.date.get(Calendar.DAY_OF_WEEK) + 5) % 7) == j && this.date.get(Calendar.MONTH) == month) {
                        Button button = new Button(getActivity());
                        button.setLayoutParams(dayLayoutParams);
                        button.setBackgroundResource(0);
                        button.setText("ciao");
                        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        button.setText(Integer.toString(this.date.get(Calendar.DAY_OF_MONTH)));
                        if (entry.getKey().get(Calendar.DAY_OF_YEAR) == this.date.get(Calendar.DAY_OF_YEAR)) {
                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
                            button.setTypeface(null, Typeface.BOLD);

                            String thisDayOfWeek = daysOfWeek[date.get(Calendar.DAY_OF_WEEK) - 1];
                            int thisDayOfMonth = date.get(Calendar.DAY_OF_MONTH);
                            String thisMonth = months[date.get(Calendar.MONTH)];
                            this.toolbarTitle = thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth;

                            final Bundle dayArgs = new Bundle();
                            dayArgs.putSerializable("date", date);
                            dayArgs.putSerializable("day", monthCalendar.get(date));
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MainActivity.toolbar.setTitle(toolbarTitle);
                                    DayFragment dayFragment = new DayFragment();
                                    dayFragment.setArguments(dayArgs);
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_container, dayFragment);
                                    transaction.addToBackStack("secondary");
                                    transaction.commit();
                                }
                            });
                            if (iterator.hasNext())
                                entry = (Map.Entry<GregorianCalendar, Map<String, String>>) iterator.next();
                        } else {
                            button.setEnabled(false);
                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.disabled_text));
                        }
                        weekLinearLayout.addView(button);
                        this.date.add(Calendar.DAY_OF_YEAR, 1);
                    } else {
                        View voidView = new View(getActivity());
                        voidView.setLayoutParams(dayLayoutParams);
                        weekLinearLayout.addView(voidView);
                    }
                }
                calendarLinearLayout.addView(weekLinearLayout);
            }
        }
        return monthView;
    }
}