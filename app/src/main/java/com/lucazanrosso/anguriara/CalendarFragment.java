package com.lucazanrosso.anguriara;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

    private int monthSelected = -1;

    public ImageView thisDayImage;
    public TextView thisDayText;
    public ImageButton juneButton;
    public ImageButton julyButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.calendar));

        this.view = inflater.inflate(R.layout.fragment_calendar, container, false);

//        thisDayImage = (ImageView) this.view.findViewById(R.id.card_view_image);
//        thisDayText = (TextView) this.view.findViewById(R.id.card_view_sub_title);

//        setThisDay();
        setNextEvenings(inflater, container);

        juneButton = (ImageButton) view.findViewById(R.id.june_button);
        julyButton = (ImageButton) view.findViewById(R.id.july_button);
        juneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthSelected(5);
            }
        });
        julyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMonthSelected(6);
            }
        });
        if (monthSelected == -1) {
            if (MainActivity.today.get(Calendar.MONTH) < 6) {
                setMonthSelected(5);
            } else {
                setMonthSelected(6);
            }
        } else {
            if (monthSelected == 5) {
                setMonthSelected(5);
            } else if (monthSelected == 6) {
                setMonthSelected(6);
            }
        }

        return this.view;
    }

    public void setMonthSelected(int month) {
        if (month == 5) {
            setMonthCalendar(5);
            juneButton.setVisibility(View.INVISIBLE);
            julyButton.setVisibility(View.VISIBLE);
            monthSelected = 5;
        } else if (month == 6){
            setMonthCalendar(6);
            julyButton.setVisibility(View.INVISIBLE);
            juneButton.setVisibility(View.VISIBLE);
            monthSelected = 6;
        }
    }

//    private void setThisDay () {
//        TextView titleTextView = (TextView) this.view.findViewById(R.id.card_view_title);
//        titleTextView.setText(CalendarFragment.setDateTitle(MainActivity.today));
//        thisDayText.setText(CalendarFragment.setDateText(MainActivity.today, getContext()));
//        thisDayImage.setImageResource(CalendarFragment.setThisDayImage(MainActivity.today));
//
//        if (MainActivity.calendar.containsKey(MainActivity.today)) {
//            CardView thisDayCardView = (CardView) this.view.findViewById(R.id.card_view);
//            final Bundle dayArgs = new Bundle();
//            dayArgs.putSerializable("date", MainActivity.today);
//            thisDayCardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
//                    dayScreenSlidePagerFragment.setArguments(dayArgs);
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.frame_container, dayScreenSlidePagerFragment);
//                    transaction.addToBackStack("secondary");
//                    transaction.commit();
//                }
//            });
//        }
//    }

    private void setNextEvenings (LayoutInflater inflater, ViewGroup container) {
        LinearLayout nextEvenings = (LinearLayout) view.findViewById(R.id.next_evenings_layout);
        int i = 0;
        for (LinkedHashMap.Entry<Calendar, LinkedHashMap<String, Object>> entry : MainActivity.calendar.entrySet())
            if (entry.getKey().get(Calendar.DAY_OF_YEAR) > MainActivity.today.get(Calendar.DAY_OF_YEAR) && i < 4) {
                i++;
                View nextEveningCard = inflater.inflate(R.layout.next_evening_card, container, false);
                TextView nextEveningsTitle = (TextView) nextEveningCard.findViewById(R.id.next_evening_title);
                TextView nextEveningsText = (TextView) nextEveningCard.findViewById(R.id.next_evening_text);
                Button detailsButton = (Button) nextEveningCard.findViewById(R.id.details_button);
                ImageView nextEveningImage = (ImageView) nextEveningCard.findViewById(R.id.next_evening_image);
                nextEveningsTitle.setText(CalendarFragment.setDateTitle(entry.getKey()));
                nextEveningsText.setText(CalendarFragment.setDateText(entry.getKey(), getContext()));
                int nextEveniningImageId = (int) entry.getValue().get("event_image");
                nextEveningImage.setImageResource(nextEveniningImageId);
                final Bundle dayArgs = new Bundle();
                dayArgs.putSerializable("date", entry.getKey());
                detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
                        dayScreenSlidePagerFragment.setArguments(dayArgs);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, dayScreenSlidePagerFragment)
                                .addToBackStack("secondary")
                                .commit();
                    }
                });
                nextEvenings.addView(nextEveningCard);
                if (i == 4) break;
            }
        if (i == 0) {
            view.findViewById(R.id.next_evenings_title).setVisibility(View.GONE);
            view.findViewById(R.id.next_evenings).setVisibility(View.GONE);
        }
    }

    private void setMonthCalendar(int month) {
        LinkedHashMap<Calendar, LinkedHashMap<String, Object>> monthCalendar = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<Calendar, LinkedHashMap<String, Object>> entry : MainActivity.calendar.entrySet()) {
            if (entry.getKey().get(Calendar.MONTH) == month) {
                monthCalendar.put(entry.getKey(), entry.getValue());
            }
        }

        Calendar date = new GregorianCalendar(MainActivity.YEAR, month, 1);
        int dateMonth = month;
        int dateDay = date.get(Calendar.DAY_OF_WEEK);

        LinearLayout monthLayout = (LinearLayout) view.findViewById(R.id.month_layout);
        monthLayout.removeAllViews();
        LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams dayLayoutParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView textView = (TextView) view.findViewById(R.id.month);

        Iterator iterator = monthCalendar.entrySet().iterator();
        if (iterator.hasNext()) {
            LinkedHashMap.Entry<Calendar, LinkedHashMap<String, Object>> entry = (LinkedHashMap.Entry<Calendar, LinkedHashMap<String, Object>>) iterator.next();

            textView.setText(MainActivity.months[month]);
            while (month == dateMonth) {
                LinearLayout weekLinearLayout = new LinearLayout(getActivity());
                weekLinearLayout.setLayoutParams(weekLayoutParams);
                for (int j = 0; j <= 6; j++) {
                    if (((dateDay + 5) % 7) == j && dateMonth == month) {
                        Button button = new Button(getActivity());
                        button.setLayoutParams(dayLayoutParams);
                        button.setPadding(0, 0, 0, 0);
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
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.frame_container, dayScreenSlidePagerFragment)
                                            .addToBackStack("secondary")
                                            .commit();
                                }
                            });
                            if (iterator.hasNext())
                                entry = (LinkedHashMap.Entry<Calendar, LinkedHashMap<String, Object>>) iterator.next();
                        } else {
                            button.setEnabled(false);
                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.disabled_text));
                        }
                        weekLinearLayout.addView(button);
                        date.add(Calendar.DAY_OF_YEAR, 1);
                        dateMonth = date.get(Calendar.MONTH);
                        dateDay = date.get(Calendar.DAY_OF_WEEK);
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

//    public static int setThisDayImage(Calendar date) {
//        if (MainActivity.calendar.containsKey(date)) {
//            if(date.equals(MainActivity.badDay)) {
//               return R.drawable.rain;
//            } else {
//                return R.drawable.open;
//            }
//        } else {
//            return R.drawable.close;
//        }
//    }
}