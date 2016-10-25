package com.lucazanrosso.anguriara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.Calendar;

public class DayScreenSlidePagerFragment extends Fragment {

    String title;

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.pager_adapter, container, false);

        MainActivity.previousFragment = R.id.day;

        Bundle args = this.getArguments();
        Calendar date = (Calendar) args.getSerializable("date");
        this.fab = (FloatingActionButton) view.findViewById(R.id.fab);
        setTitleAndFab(date);

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
                setTitleAndFab(MainActivity.days.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
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

    public void setTitleAndFab(Calendar date) {
        title = CalendarFragment.setDateTitle(date);
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(title);
//        MainActivity.toolbar.setTitle(title);
        title += " " + getResources().getString(R.string.share_title) + "\n" + CalendarFragment.setDateText(date, getContext()) + "\n\n" + getResources().getString(R.string.share_text) + "\n" + Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getPackageName()).toString();
//        if (! MainActivity.calendar.get(date).get("event").isEmpty()) {
//            title += "\n" + getResources().getString(R.string.event) + ": " + MainActivity.calendar.get(date).get("event");
//            if (! MainActivity.calendar.get(date).get("food").isEmpty())
//                title += "\n" + getResources().getString(R.string.food) + ": " + MainActivity.calendar.get(date).get("food");
//        } else
//            title += "\n" + getResources().getString(R.string.open);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
//                Uri uri = Uri.parse("android.resource://com.lucazanrosso.anguriara/" + R.drawable.outsiders);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, title);
                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                shareIntent.setType("image/*");
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(shareIntent);
            }
        });
    }
}