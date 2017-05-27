package com.lucazanrosso.anguriara;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    View view;

    private int monthSelected = -1;
    private final int JUNE = 5;
    private final int JULY = 6;
    public ImageButton juneButton;
    public ImageButton julyButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(getResources().getString(R.string.calendar));
        MainActivity.previousFragment = R.id.calendar;

        this.view = inflater.inflate(R.layout.fragment_calendar, container, false);

        setAllEvenings();

        juneButton = (ImageButton) view.findViewById(R.id.june_button);
        julyButton = (ImageButton) view.findViewById(R.id.july_button);
        juneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthSelected(JUNE);
            }
        });
        julyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthSelected(JULY);
            }
        });
        if (savedInstanceState == null) {
            if (monthSelected == -1) {
                // I have to use this check due to a problem of Android Studio     if (MainActivity.YEAR == MainActivity.today.get(Calendar.YEAR) && MainActivity.today.get(Calendar.MONTH) == 6)
                if ((MainActivity.YEAR < MainActivity.today.get(Calendar.YEAR) || MainActivity.YEAR > MainActivity.today.get(Calendar.YEAR)) || (MainActivity.today.get(Calendar.MONTH) < JULY || MainActivity.today.get(Calendar.MONTH) > JULY))
                    monthSelected = JUNE;
                else
                    monthSelected = JULY;
            }
        } else {
            monthSelected = savedInstanceState.getInt("month_selected");
        }
        setMonthSelected(monthSelected);

        return this.view;
    }

    public void setMonthSelected(int month) {
        if (month == JUNE) {
            setMonthCalendar2(JUNE);
            juneButton.setVisibility(View.INVISIBLE);
            julyButton.setVisibility(View.VISIBLE);
            monthSelected = JUNE;
        } else if (month == JULY){
            setMonthCalendar2(JULY);
            julyButton.setVisibility(View.INVISIBLE);
            juneButton.setVisibility(View.VISIBLE);
            monthSelected = JULY;
        }
    }

    private void setAllEvenings () {
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.all_evenings_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.Adapter mAdapter = new AllEveningsAdapter(getActivity());

        int i = 0;
        for (Calendar evening : MainActivity.days) {
            if (MainActivity.today.after(evening))
                i++;
            else break;
        }
        mLayoutManager.scrollToPosition(i);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setMonthCalendar2(int month) {
        ArrayList<Calendar> monthCalendar = new ArrayList<>();
        for (Calendar day : MainActivity.days) {
            if (day.get(Calendar.MONTH) == month) {
                monthCalendar.add(day);
            }
        }

        Calendar date = new GregorianCalendar(MainActivity.YEAR, month, 1);

        LinearLayout monthLayout = (LinearLayout) view.findViewById(R.id.month_layout);
        monthLayout.removeAllViews();
        LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams dayLayoutParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView textView = (TextView) view.findViewById(R.id.month);

        ListIterator iterator = monthCalendar.listIterator();
        if (iterator.hasNext()) {
            Calendar day = (Calendar) iterator.next();

            textView.setText(MainActivity.months[month]);
            while (date.get(Calendar.MONTH) == month) {
                LinearLayout weekLinearLayout = new LinearLayout(getActivity());
                weekLinearLayout.setLayoutParams(weekLayoutParams);
                for (int j = 0; j <= 6; j++) {
                    if (((date.get(Calendar.DAY_OF_WEEK) + 5) % 7) == j && date.get(Calendar.MONTH) == month) {
                        Button button = new Button(getActivity());
                        button.setLayoutParams(dayLayoutParams);
                        button.setPadding(0, 0, 0, 0);
                        button.setBackgroundResource(0);
                        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        button.setText(String.format(Locale.getDefault(), "%d", date.get(Calendar.DAY_OF_MONTH)));
                        if (day.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)) {
                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
                            button.setTypeface(null, Typeface.BOLD);
                            final Bundle dayArgs = new Bundle();
                            dayArgs.putSerializable("date", day);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
                                    dayScreenSlidePagerFragment.setArguments(dayArgs);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation, R.anim.enter_animation, R.anim.exit_animation)
                                            .replace(R.id.frame_container, dayScreenSlidePagerFragment)
                                            .addToBackStack("secondary")
                                            .commit();
                                }
                            });
                            if (iterator.hasNext())
                                day = (Calendar) iterator.next();
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

    public static String setDateTitle(Calendar date) {
        String thisDayOfWeek = MainActivity.daysOfWeek[date.get(Calendar.DAY_OF_WEEK) - 1];
        int thisDayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        String thisMonth = MainActivity.months[date.get(Calendar.MONTH)];
        return thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth;
    }

    public static String setDateText(Calendar date, Context context) {
        if (MainActivity.calendar.containsKey(date)) {
            if(date.equals(MainActivity.badDay)) {
                return context.getResources().getString(R.string.bad_weather);
            } else {
                String dayEventAndFood = (String) MainActivity.calendar.get(date).get("event");
                String dayFood = (String) MainActivity.calendar.get(date).get("food");
                if (!dayFood.isEmpty())
                    dayEventAndFood += "\n" + context.getResources().getString(R.string.food) + ": " + dayFood;
                return  dayEventAndFood;
            }
        } else {
            return context.getResources().getString(R.string.close);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("month_selected", monthSelected);
    }
}