package com.luca.anguriara;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Luca on 23/08/2015.
 */
public class MonthFragment extends Fragment{

    View view;
    Map<GregorianCalendar, LinkedHashMap<String, String>> monthCalendar;
    int month;
    Calendar date;
    String[] months;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_month, container, false);
        Bundle args = this.getArguments();
        this.monthCalendar = (Map<GregorianCalendar, LinkedHashMap<String, String>>) args.getSerializable("calendar");
        this.month = args.getInt("month");
        this.date = new GregorianCalendar(2015, this.month, 1);
        months = getResources().getStringArray(R.array.months);

        LinearLayout calendarLinearLayout = (LinearLayout) view.findViewById(R.id.calendar_layout);
        LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams dayLayoutParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView textView = (TextView) view.findViewById(R.id.month);

        Iterator iterator = this.monthCalendar.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<GregorianCalendar, Map<String, String>> entry = (Map.Entry) iterator.next();

            textView.setText(months[month]);
            for (int i = 1; i <= this.date.get(Calendar.DAY_OF_MONTH); i++) {
                LinearLayout weekLinearLayout = new LinearLayout(getActivity());
                weekLinearLayout.setLayoutParams(weekLayoutParams);
                for (int j = 0; j <= 6; j++) {
                    Button button = new Button(getActivity());
                    button.setLayoutParams(dayLayoutParams);
                    button.setBackgroundResource(0);
                    if (((this.date.get(Calendar.DAY_OF_WEEK) + 5) % 7) == j && this.date.get(Calendar.MONTH) == this.month) {
                        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        button.setText(Integer.toString(this.date.get(Calendar.DAY_OF_MONTH)));
                        if (entry.getKey().get(Calendar.DAY_OF_YEAR) == this.date.get(Calendar.DAY_OF_YEAR)) {
                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
                            button.setTypeface(null, Typeface.BOLD);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Fragment dayFragment = new DayFragment();
                                    Bundle args = new Bundle();
                                    args.putSerializable("date", date);
                                    args.putSerializable("day", monthCalendar.get(date));
                                    dayFragment.setArguments(args);
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_container, dayFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            });
                            if (iterator.hasNext()) {
                                entry = (Map.Entry) iterator.next();
                            }
                        } else {
                            button.setEnabled(false);
                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.disabled_text));
                        }
                        this.date.add(Calendar.DAY_OF_YEAR, 1);
                    }
                    weekLinearLayout.addView(button);
                }
                calendarLinearLayout.addView(weekLinearLayout);
            }
        }
        return view;
    }
}
