package com.lucazanrosso.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle(getResources().getString(R.string.anguriara));

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView titleTextView = (TextView) view.findViewById(R.id.this_day_title);
        titleTextView.setText(CalendarFragment.setDateTitle(MainActivity.today));

        return view;
    }
}