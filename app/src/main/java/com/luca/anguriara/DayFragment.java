package com.luca.anguriara;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by Luca on 27/09/2015.
 */
public class DayFragment extends Fragment{

    View view;
    Calendar date;
    Map<String, String> day;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);
        Bundle args = this.getArguments();
        this.date = (Calendar) args.getSerializable("date");
        this.day = (Map<String, String>) args.getSerializable("day");

//        TextView textView = (TextView) view.findViewById(R.id.event);
//        textView.setText(day.get("food"));

        return view;
    }
}
