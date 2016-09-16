package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    View view;
    ImageView [] slide_circles = new ImageView[5];
    int currentPosition;
    int previousPosition;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.home));

        this.view = inflater.inflate(R.layout.fragment_home, container, false);
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

        getThisDay();

        return view;
    }

    private void getThisDay() {
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