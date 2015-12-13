package com.lucazanrosso.anguriara;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MonthFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        Bundle args = this.getArguments();
        int month = args.getInt("month");
        Calendar date = new GregorianCalendar(2015, month, 1);
        String[] daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        String[] months = getResources().getStringArray(R.array.months);

        LinearLayout calendarLinearLayout = (LinearLayout) view.findViewById(R.id.month_layout);
        LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams dayLayoutParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView textView = (TextView) view.findViewById(R.id.month);

        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> monthCalendar = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
            if (entry.getKey().get(Calendar.MONTH) == month) {
                monthCalendar.put(entry.getKey(), entry.getValue());
            }
        }

        Iterator iterator = monthCalendar.entrySet().iterator();
        if (iterator.hasNext()) {
            LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry = (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>>) iterator.next();

            textView.setText(months[month]);
            for (int i = 1; i <= date.get(Calendar.DAY_OF_MONTH); i++) {
                LinearLayout weekLinearLayout = new LinearLayout(getActivity());
                weekLinearLayout.setLayoutParams(weekLayoutParams);
                for (int j = 0; j <= 6; j++) {
                    Button button = new Button(getActivity());
                    button.setLayoutParams(dayLayoutParams);
                    button.setBackgroundResource(0);
                    if (((date.get(Calendar.DAY_OF_WEEK) + 5) % 7) == j && date.get(Calendar.MONTH) == month) {
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
                            if (iterator.hasNext()) {
                                entry = (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>>) iterator.next();
                            }
                        } else {
                            button.setEnabled(false);
                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.disabled_text));
                        }
                        date.add(Calendar.DAY_OF_YEAR, 1);
                    }
                    weekLinearLayout.addView(button);
                }
                calendarLinearLayout.addView(weekLinearLayout);
            }
        }
        return view;

//        String[] months = getResources().getStringArray(R.array.months);
//        Bundle args = this.getArguments();
//        int month = args.getInt("month");
//
//        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> monthCalendar = new LinkedHashMap<>();
//        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
//            if (entry.getKey().get(Calendar.MONTH) == month) {
//                monthCalendar.put(entry.getKey(), entry.getValue());
//            }
//        }
//
//        Calendar date = new GregorianCalendar(2015, month, 1);
//
//        LinearLayout monthLayout = (LinearLayout) view.findViewById(R.id.month_layout);
//        LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams dayLayoutParams = new LinearLayout.LayoutParams(
//                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
//        TextView textView = (TextView) view.findViewById(R.id.month);
//
//        Iterator iterator = monthCalendar.entrySet().iterator();
//        if (iterator.hasNext()) {
//            LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry = (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>>) iterator.next();
//
//            textView.setText(months[month]);
//            while (month == date.get(Calendar.MONTH)) {
//                LinearLayout weekLinearLayout = new LinearLayout(getActivity());
//                weekLinearLayout.setLayoutParams(weekLayoutParams);
//                for (int j = 0; j <= 6; j++) {
//                    Log.d("month", "month:" + month);
//                    if (((date.get(Calendar.DAY_OF_WEEK) + 5) % 7) == j && date.get(Calendar.MONTH) == month) {
//                        Button button = new Button(getActivity());
//                        button.setLayoutParams(dayLayoutParams);
//                        button.setBackgroundResource(0);
//                        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                        button.setText(Integer.toString(date.get(Calendar.DAY_OF_MONTH)));
//                        if (entry.getKey().get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)) {
//                            Log.d("month", "month4:" + date.get(Calendar.DAY_OF_YEAR));
//                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
//                            button.setTypeface(null, Typeface.BOLD);
//
//                            final Bundle dayArgs = new Bundle();
//                            dayArgs.putSerializable("date", entry.getKey());
//                            button.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
//                                    dayScreenSlidePagerFragment.setArguments(dayArgs);
//                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                    transaction.replace(R.id.frame_container, dayScreenSlidePagerFragment);
//                                    transaction.addToBackStack("secondary");
//                                    transaction.commit();
//                                }
//                            });
//                            if (iterator.hasNext())
//                                entry = (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>>) iterator.next();
//                        } else {
//                            button.setEnabled(false);
//                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.disabled_text));
//                        }
//                        Log.d("month", "add");
//                        weekLinearLayout.addView(button);
//                        date.add(Calendar.DAY_OF_YEAR, 1);
//                    } else {
//                        View voidView = new View(getActivity());
//                        voidView.setLayoutParams(dayLayoutParams);
//                        weekLinearLayout.addView(voidView);
//                    }
//                }
//                monthLayout.addView(weekLinearLayout);
//            }
//        }
//        return view;
    }
}
