package com.lucazanrosso.anguriara;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class CalendarFragment extends Fragment {

    View view;

    //TO IMPROVE
    private int monthSelected = -1;

    private Calendar today = new GregorianCalendar(2016,5,10);
    private String[] daysOfWeek;
    private String[] months;

    public static ImageView thisDayImage;
    public static TextView thisDayText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.calendar));

        this.view = inflater.inflate(R.layout.fragment_calendar, container, false);

        this.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        this.months = getResources().getStringArray(R.array.months);

//        if (savedInstanceState == null)
//            getFirebaseDatabase();
        setThisDay();

//        setNextEvening(inflater, container);

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

//        TO IMPROVE
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
        CalendarFragment.thisDayImage = (ImageView) this.view.findViewById(R.id.card_view_image);
        TextView titleTextView = (TextView) this.view.findViewById(R.id.card_view_title);
        CalendarFragment.thisDayText = (TextView) this.view.findViewById(R.id.card_view_sub_title);
        CardView thisDayCardView = (CardView) this.view.findViewById(R.id.card_view);

        String thisDayOfWeek = daysOfWeek[this.today.get(Calendar.DAY_OF_WEEK) - 1];
        int thisDayOfMonth = this.today.get(Calendar.DAY_OF_MONTH);
        String thisMonth = months[this.today.get(Calendar.MONTH)];
        String cardViewTitle = thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth;
        titleTextView.setText(cardViewTitle);

        if (MainActivity.calendar.containsKey(this.today)) {
            if(MainActivity.badDay != null && MainActivity.today.get(Calendar.DAY_OF_MONTH) == MainActivity.badDay.get(Calendar.DAY_OF_MONTH) && MainActivity.today.get(Calendar.MONTH) == MainActivity.badDay.get(Calendar.MONTH) && MainActivity.today.get(Calendar.YEAR) == MainActivity.badDay.get(Calendar.YEAR)) {
                CalendarFragment.thisDayImage.setImageResource(R.drawable.close);
                CalendarFragment.thisDayText.setText(getResources().getString(R.string.bad_weather));
            } else {
                CalendarFragment.thisDayImage.setImageResource(R.drawable.open);
                String dayEventAndFood = getResources().getString(R.string.event) + ": " + MainActivity.calendar.get(this.today).get("event") + "\n";
                if (!MainActivity.calendar.get(this.today).get("food").isEmpty())
                    dayEventAndFood += getResources().getString(R.string.food) + ": " + MainActivity.calendar.get(this.today).get("food");
                CalendarFragment.thisDayText.setText(dayEventAndFood);

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
            }
        } else {
            CalendarFragment.thisDayImage.setImageResource(R.drawable.close);
            CalendarFragment.thisDayText.setText(getResources().getString(R.string.close));
        }
    }

//    private void setNextEvening(LayoutInflater inflater, ViewGroup container) {
//
////        Resources r = getActivity().getResources();
////        int px = (int) TypedValue.applyDimension(
////                TypedValue.COMPLEX_UNIT_DIP,
////                84,
////                r.getDisplayMetrics()
////        );
////
////        LinearLayout nextEvening = (LinearLayout) view.findViewById(R.id.next_evening_layout);
////
////        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
////                500, px);
////        cardLayoutParams.setMargins(16, 8, 16, 32);
//
//        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet())
//            if (entry.getKey().get(Calendar.DAY_OF_YEAR) > today.get(Calendar.DAY_OF_YEAR)) {
//
//                View nextEveningCard = inflater.inflate(R.layout.next_evening_card, container, false);
//                TextView nextEveningTitle = (TextView) nextEveningCard.findViewById(R.id.next_evening_title);
//                TextView nextEveningText = (TextView) nextEveningCard.findViewById(R.id.next_evening_text);
//                nextEveningTitle.setText(MainActivity.setDateTitle(entry.getKey()));
//                String nextEveningContent = getResources().getString(R.string.event) + ": " + MainActivity.calendar.get(entry.getKey()).get("event") + "\n";
//                if (! MainActivity.calendar.get(entry.getKey()).get("food").isEmpty())
//                    nextEveningContent += getResources().getString(R.string.food) + ": " + MainActivity.calendar.get(entry.getKey()).get("food");
//                nextEveningText.setText(nextEveningContent);
//                nextEvening.addView(nextEveningCard);
//                final Bundle dayArgs = new Bundle();
//                dayArgs.putSerializable("date", entry.getKey());
//                nextEveningCard.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
//                        dayScreenSlidePagerFragment.setArguments(dayArgs);
//                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.frame_container, dayScreenSlidePagerFragment);
//                        transaction.addToBackStack("secondary");
//                        transaction.commit();
//                    }
//                });
//
////                CardView cardView = new CardView(getActivity());
////                TextView textView = new TextView(getActivity());
////                textView.setText("ciao");
////                cardView.setLayoutParams(cardLayoutParams);
////                cardView.setCardBackgroundColor(R.color.accent);
////                cardView.addView(textView);
////                cardView.setRadius(8);
////                nextEvening.addView(cardView);
////                Log.d("ciao", "ciao");
//            }
//    }

    private void setMonthCalendar(int month) {
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> monthCalendar = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
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
            LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry = (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>>) iterator.next();

            textView.setText(months[month]);
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

//    public void getFirebaseDatabase() {
////        final ImageView imageView = (ImageView) this.view.findViewById(R.id.card_view_image);
////        final TextView subTitleTextView = (TextView) this.view.findViewById(R.id.card_view_sub_title);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("bad_weather");
////        myRef.setValue(today);
//        myRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Log.d("Ciao", "onChildChanged:" + dataSnapshot.getKey() + " " + dataSnapshot.getValue(Long.class));
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                Log.d("day", "day" + dataSnapshot.child("day").getValue());
//                CalendarFragment.badDay = new GregorianCalendar(dataSnapshot.child("year").getValue(Integer.class), dataSnapshot.child("month").getValue(Integer.class), dataSnapshot.child("day").getValue(Integer.class));
////                Log.d("day", "day" + MainActivity.badDay.toString());
//                if(CalendarFragment.badDay != null && today.get(Calendar.DAY_OF_MONTH) == CalendarFragment.badDay.get(Calendar.DAY_OF_MONTH) && today.get(Calendar.MONTH) == CalendarFragment.badDay.get(Calendar.MONTH) && today.get(Calendar.YEAR) == CalendarFragment.badDay.get(Calendar.YEAR)) {
//                    CalendarFragment.thisDayImage.setImageResource(R.drawable.close);
//                    CalendarFragment.thisDayText.setText(getResources().getString(R.string.bad_weather));
//                }
////                CalendarFragment calendarFragment = new CalendarFragment();
////                getSupportFragmentManager().beginTransaction()
////                        .replace(R.id.frame_container, calendarFragment).commit();
////                Map<String, Long> map = dataSnapshot.getValue(Map.class<String.class, Long.class>);
////                Log.d("ciao", "ciao"  + dataSnapshot.getValue());
////                Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
////                Log.d("Ciao", "Value is: " + value);
////                MainActivity.badWeather = value.equals("Y");
////                imageView.setImageResource(R.drawable.close);
////                subTitleTextView.setText(getResources().getString(R.string.bad_weather));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}