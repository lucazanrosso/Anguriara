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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DayScreenSlidePagerFragment extends Fragment {

    String[] daysOfWeek;
    String[] months;
    String thisDayOfWeek;
    int thisDayOfMonth;
    String thisMonth;
    String title;

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.pager_adapter, container, false);

        Bundle args = this.getArguments();
        GregorianCalendar date = (GregorianCalendar) args.getSerializable("date");

        this.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        this.months = getResources().getStringArray(R.array.months);

        this.thisDayOfWeek = daysOfWeek[date.get(Calendar.DAY_OF_WEEK) - 1];
        this.thisDayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        this.thisMonth = months[date.get(Calendar.MONTH)];
        title = thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth;
        MainActivity.toolbar.setTitle(title);

        this.title += " " + getResources().getString(R.string.share_title);
        this.fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (! MainActivity.calendar.get(date).get("event").isEmpty()) {
            title += "\n" + getResources().getString(R.string.event) + ": " + MainActivity.calendar.get(date).get("event");
            if (! MainActivity.calendar.get(date).get("food").isEmpty())
                title += "\n" + getResources().getString(R.string.food) + ": " + MainActivity.calendar.get(date).get("food");
        } else
            title += "\n" + getResources().getString(R.string.open);
        title += "\n\n" + getResources().getString(R.string.share_text) + "\n" + Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getPackageName()).toString();
        Log.d("package",getContext().getPackageName().toString());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, title);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
//                Uri imageUri = Uri.parse("file//res/drawable/logo.png");
//                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://"
//                        + getContext().getPackageName() + "/"
//                        + R.drawable.logo));
//                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://com.lucazanrosso.anguriara/drawable/logo"));
//                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(R.drawable.logo) + '/' + getResources().getResourceTypeName(R.drawable.logo) + '/' + getResources().getResourceEntryName(R.drawable.logo)));
//                sendIntent.setType("image/png");

//                startActivity(Intent.createChooser(sendIntent, "image"));
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getContext().getPackageName())));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
//                }
            }
        });

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

                daysOfWeek = getResources().getStringArray(R.array.days_of_week);
                months = getResources().getStringArray(R.array.months);

                thisDayOfWeek = daysOfWeek[date.get(Calendar.DAY_OF_WEEK) - 1];
                thisDayOfMonth = date.get(Calendar.DAY_OF_MONTH);
                thisMonth = months[date.get(Calendar.MONTH)];
                title = thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth;
                MainActivity.toolbar.setTitle(title);

                title += " " + getResources().getString(R.string.share_title);
                if (! MainActivity.calendar.get(date).get("event").isEmpty()) {
                    title += "\n" + getResources().getString(R.string.event) + ": " + MainActivity.calendar.get(date).get("event");
                    if (! MainActivity.calendar.get(date).get("food").isEmpty())
                        title += "\n" + getResources().getString(R.string.food) + ": " + MainActivity.calendar.get(date).get("food");
                } else
                    title += "\n" + getResources().getString(R.string.open);
                title += "\n\n" + getResources().getString(R.string.share_text) + "\n" + Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getPackageName()).toString();

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, title);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });
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