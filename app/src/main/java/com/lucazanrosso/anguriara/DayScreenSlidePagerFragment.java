package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DayScreenSlidePagerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.pager_adapter, container, false);

        Bundle args = this.getArguments();
        GregorianCalendar date = (GregorianCalendar) args.getSerializable("date");

        String[] daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        String[] months = getResources().getStringArray(R.array.months);

        String thisDayOfWeek = daysOfWeek[date.get(Calendar.DAY_OF_WEEK) - 1];
        int thisDayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        String thisMonth = months[date.get(Calendar.MONTH)];
        MainActivity.toolbar.setTitle(thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth);

        ViewPager mPager = (ViewPager) view.findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(MainActivity.days.indexOf(date));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GregorianCalendar date = MainActivity.days.get(position);

                String[] daysOfWeek = getResources().getStringArray(R.array.days_of_week);
                String[] months = getResources().getStringArray(R.array.months);

                String thisDayOfWeek = daysOfWeek[date.get(Calendar.DAY_OF_WEEK) - 1];
                int thisDayOfMonth = date.get(Calendar.DAY_OF_MONTH);
                String thisMonth = months[date.get(Calendar.MONTH)];
                MainActivity.toolbar.setTitle(thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle dayArgs = new Bundle();
            dayArgs.putSerializable("date", MainActivity.days.get(position));
            DayFragment dayFragment = new DayFragment();
            dayFragment.setArguments(dayArgs);
            return dayFragment;
        }

        @Override
        public int getCount() {
            return MainActivity.ANGURIARA_NUMBER_OF_DAYS;
        }
    }
}