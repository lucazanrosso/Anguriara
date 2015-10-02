package com.luca.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Map;

public class DayFragment extends Fragment{

    View view;
    private Calendar date;
    private Map<String, String> day;
    private TextView eventTextView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);
        Bundle args = this.getArguments();
        this.date = (Calendar) args.getSerializable("date");
        this.day = (Map<String, String>) args.getSerializable("day");

        eventTextView = (TextView) view.findViewById(R.id.event);
        eventTextView.setText(day.get("food"));

        return view;
    }
}
