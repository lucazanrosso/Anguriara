package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    View view;
    ImageView [] slide_circles = new ImageView[5];
    int currentPosition;
    int previousPosition;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(getResources().getString(R.string.home));

        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        if (MainActivity.days.get(MainActivity.ANGURIARA_NUMBER_OF_DAYS - 1).get(Calendar.DAY_OF_YEAR) < MainActivity.today.get(Calendar.DAY_OF_YEAR)) {
            ((ImageView) view.findViewById(R.id.welcome_image)).setImageResource(R.drawable.anguriara2016post);
        }

        slide_circles[0] = (ImageView) view.findViewById(R.id.circle0);
        slide_circles[1] = (ImageView) view.findViewById(R.id.circle1);
        slide_circles[2] = (ImageView) view.findViewById(R.id.circle2);
        slide_circles[3] = (ImageView) view.findViewById(R.id.circle3);
        slide_circles[4] = (ImageView) view.findViewById(R.id.circle4);

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

        setThisDay();

        return view;
    }

    private void setThisDay() {
        ImageView todayImage = (ImageView) view.findViewById(R.id.event_image);
        TextView todayTitle = (TextView) view.findViewById(R.id.event_title);
        TextView todayText = (TextView) view.findViewById(R.id.event_text);
        Button detailsButton = (Button) view.findViewById(R.id.details_button);
        Button scheduleButton = (Button) view.findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
//                        .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
                        .replace(R.id.frame_container, new CalendarFragment())
                        .addToBackStack("secondary")
                        .commit();
            }
        });

        if (MainActivity.calendar.containsKey(MainActivity.today)) {
            if(MainActivity.today.equals(MainActivity.badDay)) {
                todayTitle.setText(getResources().getString(R.string.bad_weather));
                todayImage.setVisibility(View.GONE);
                todayText.setVisibility(View.GONE);
                detailsButton.setVisibility(View.GONE);
            } else {
                todayTitle.setText((String) MainActivity.calendar.get(MainActivity.today).get("event"));
                todayImage.setImageResource((int) MainActivity.calendar.get(MainActivity.today).get("event_image"));
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
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
//                                .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation, R.anim.enter_animation, R.anim.exit_animation)
                                .replace(R.id.frame_container, dayScreenSlidePagerFragment)
                                .addToBackStack("secondary")
                                .commit();
                    }
                });
            }
        } else {
            todayTitle.setText(getResources().getString(R.string.close));
            todayImage.setVisibility(View.GONE);
            todayText.setVisibility(View.GONE);
            detailsButton.setVisibility(View.GONE);
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
            return 5;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        slide_circles[currentPosition].setImageResource(R.drawable.circle_full);
    }
}