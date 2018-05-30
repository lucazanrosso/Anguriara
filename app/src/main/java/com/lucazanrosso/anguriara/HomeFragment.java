package com.lucazanrosso.anguriara;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import java.util.LinkedHashMap;

public class HomeFragment extends Fragment {

    View view;
    ImageView [] slide_circles;
    final int NUMBER_OF_SLIDES = 5;
    int currentPosition;
    int previousPosition;

    SharedPreferences sharedPreferences;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(getResources().getString(R.string.home));
        MainActivity.previousFragment = R.id.home;

        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        if (MainActivity.days.get(MainActivity.days.size() - 1).get(Calendar.DAY_OF_YEAR) < MainActivity.today.get(Calendar.DAY_OF_YEAR)) {
            ((ImageView) view.findViewById(R.id.welcome_image)).setImageResource(R.drawable.anguriara2017post);
        }

        setSlider();

        setNews();

        setThisDay();

        setNextEvenings(inflater, container);

        return view;
    }

    private void setSlider() {
        slide_circles = new ImageView[NUMBER_OF_SLIDES];
        LinearLayout circlesContainer = (LinearLayout) view.findViewById(R.id.circles_container);
        LinearLayout.LayoutParams circleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int paddingLeftRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getContext().getResources().getDisplayMetrics());
        for (int i = 0; i < NUMBER_OF_SLIDES; i++) {
            ImageView circleImage = new ImageView(getContext());
            circleImage.setLayoutParams(circleParams);
            circleImage.setImageResource(R.drawable.circle);
            circleImage.setPadding(paddingLeftRight, 0, paddingLeftRight, 0);
            slide_circles[i] = circleImage;
            circlesContainer.addView(circleImage);
        }

        ImageViewPager mPager = (ImageViewPager) view.findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new HomeFragment.ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                slide_circles[position].setImageResource(R.drawable.circle_full);
                slide_circles[previousPosition].setImageResource(R.drawable.circle);
                previousPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setNews() {
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        GregorianCalendar newsDay = new GregorianCalendar(sharedPreferences.getInt("NewsYear", 0), sharedPreferences.getInt("NewsMonth", 0), sharedPreferences.getInt("NewsDay", 0));
        if (sharedPreferences.getBoolean("News", false) && MainActivity.today.equals(newsDay)) {
            TextView newsTitle = view.findViewById(R.id.news_title);
            CardView newsCardView = view.findViewById(R.id.news_card_view);
            TextView newsText = view.findViewById(R.id.news_text);
            newsText.setText(sharedPreferences.getString("NotificationText", null));
            newsTitle.setVisibility(View.VISIBLE);
            newsCardView.setVisibility(View.VISIBLE);
        }
    }

    private void setThisDay() {
        // IMAGE 4
//        ImageView todayImage = view.findViewById(R.id.event_image);
        TextView todayDay = view.findViewById(R.id.event_day);
        todayDay.setText(CalendarFragment.setDateTitle(MainActivity.today));
        TextView todayTitle = view.findViewById(R.id.event_title);
        TextView todayText = view.findViewById(R.id.event_text);
        Button detailsButton = view.findViewById(R.id.details_button);
        Button scheduleButton = view.findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation, R.anim.enter_animation, R.anim.exit_animation)
                        .replace(R.id.frame_container, new CalendarFragment())
                        .addToBackStack("secondary").commit();
            }
        });

        if (MainActivity.days.contains(MainActivity.today)) {
            if(MainActivity.today.equals(MainActivity.badDay)) {
                todayTitle.setText(getResources().getString(R.string.bad_weather));
//                todayImage.setVisibility(View.GONE);
                todayText.setVisibility(View.GONE);
                detailsButton.setVisibility(View.GONE);
            } else {
                todayTitle.setText((String) MainActivity.calendar.get(MainActivity.today).get("event"));
//                todayImage.setImageResource((int) MainActivity.calendar.get(MainActivity.today).get("event_image"));
                String foodString = (String) MainActivity.calendar.get(MainActivity.today).get("food");
                if (!foodString.isEmpty())
                    todayText.setText(getResources().getString(R.string.food) + ": " + foodString + "\n" + getResources().getString(R.string.opening_time) + ": " + MainActivity.calendar.get(MainActivity.today).get("openingTime"));
                else
                    todayText.setText(getResources().getString(R.string.opening_time) + ": " + MainActivity.calendar.get(MainActivity.today).get("openingTime"));
                final Bundle dayArgs = new Bundle();
                dayArgs.putSerializable("date", MainActivity.today);
                detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DayScreenSlidePagerFragment dayScreenSlidePagerFragment = new DayScreenSlidePagerFragment();
                        dayScreenSlidePagerFragment.setArguments(dayArgs);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation, R.anim.enter_animation, R.anim.exit_animation)
                                .replace(R.id.frame_container, dayScreenSlidePagerFragment)
                                .addToBackStack("secondary").commit();
                    }
                });
            }
        } else {
            todayTitle.setText(getResources().getString(R.string.close));
//            todayImage.setVisibility(View.GONE);
            todayText.setVisibility(View.GONE);
            detailsButton.setVisibility(View.GONE);
        }
    }

    private void setNextEvenings (LayoutInflater inflater, ViewGroup container) {
        // IMAGE 3
        LinearLayout nextEvenings = view.findViewById(R.id.next_evenings_layout);
        int i = 0;
        for (LinkedHashMap.Entry<Calendar, LinkedHashMap<String, Object>> entry : MainActivity.calendar.entrySet())
            if (MainActivity.today.before(entry.getKey())) {
                i++;
                View nextEveningCard = inflater.inflate(R.layout.next_evening_card, container, false);
                TextView nextEveningsTitle = nextEveningCard.findViewById(R.id.next_evening_title);
                TextView nextEveningsText = nextEveningCard.findViewById(R.id.next_evening_text);
                Button detailsButton = nextEveningCard.findViewById(R.id.details_button);
//                ImageView nextEveningImage = nextEveningCard.findViewById(R.id.next_evening_image);
                nextEveningsTitle.setText(CalendarFragment.setDateTitle(entry.getKey()));
                nextEveningsText.setText(CalendarFragment.setDateText(entry.getKey(), getContext()));
//                int nextEveniningImageId = (int) entry.getValue().get("event_image");
//                nextEveningImage.setImageResource(nextEveniningImageId);
                final Bundle dayArgs = new Bundle();
                dayArgs.putSerializable("date", entry.getKey());
                detailsButton.setOnClickListener(new View.OnClickListener() {
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
                nextEvenings.addView(nextEveningCard);
                if (i == 4) break;
            }
        if (i == 0) {
            view.findViewById(R.id.next_evenings_title).setVisibility(View.GONE);
            view.findViewById(R.id.next_evenings).setVisibility(View.GONE);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt("position", position);
            HomeSlideFragment homeSlideFragment = new HomeSlideFragment();
            homeSlideFragment.setArguments(args);
            return homeSlideFragment;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_SLIDES;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        slide_circles[currentPosition].setImageResource(R.drawable.circle_full);
    }
}