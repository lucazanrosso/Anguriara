package com.luca.anguriara;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Luca on 11/09/2015.
 */
public class CalendarFragment extends Fragment {

    View view;
    Context context;
    private String fileName = "anguriara.ser";
    private Map<GregorianCalendar, Map<String, String>> calendar = new LinkedHashMap<>();
    final static int YEAR = 2015;
    final static int ANGURIARA_NUMBER_OF_DAYS = 31;
    private Calendar today = new GregorianCalendar();
    private String[] daysOfWeek;
    private String[] months;
    private int[] anguriaraMonths;
    private int[] anguriaraDaysOfMonth;
    private String[] anguriaraEvents;
    private String[] anguriaraFoods;
    private String[] anguriaraOpeningTimes;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_calendar, container, false);
        this.context = view.getContext();
        this.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        this.months = getResources().getStringArray(R.array.months);
        this.anguriaraMonths = getResources().getIntArray(R.array.anguriara_months);
        this.anguriaraDaysOfMonth = getResources().getIntArray(R.array.anguriara_days_of_month);
        this.anguriaraEvents = getResources().getStringArray(R.array.anguriara_events);
        this.anguriaraFoods = getResources().getStringArray(R.array.anguriara_foods);


//        File file = new File(this.context.getFilesDir(), this.fileName);
//        if (file.exists()) {
//            this.calendar = deserializeCalendar();
//        } else {
            this.calendar = setCalendar();
            serializeCalendar(this.calendar);
//        }

        thisDay();
        setMonthCalendar(Calendar.JUNE, R.id.june_calendar);
        setMonthCalendar(Calendar.JULY, R.id.july_calendar);

        return this.view;
    }

    public Map<GregorianCalendar, Map<String, String>> setCalendar() {
        Map<GregorianCalendar, Map<String, String>> calendar = new LinkedHashMap<>();
        for (int i = 0; i < ANGURIARA_NUMBER_OF_DAYS; i++) {
            Map<String, String> eveningMap = new LinkedHashMap<>();
            eveningMap.put("event", anguriaraEvents[i]);
            eveningMap.put("food", anguriaraFoods[i]);
            calendar.put(new GregorianCalendar(YEAR, anguriaraMonths[i], anguriaraDaysOfMonth[i]), eveningMap);
        }
        return calendar;
    }

    public void serializeCalendar(Map<GregorianCalendar, Map<String, String>> calendar) {
        try {
            File file = new File(this.context.getFilesDir(), this.fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(calendar);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<GregorianCalendar, Map<String, String>> deserializeCalendar(){
        Map<GregorianCalendar, Map<String, String>> calendar = new LinkedHashMap<>();
        try {
            File file = new File(this.context.getFilesDir(), this.fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            calendar = (LinkedHashMap<GregorianCalendar, Map<String, String>>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public void thisDay () {
        ImageView imageView = (ImageView) this.view.findViewById(R.id.card_view_image);
        TextView titleTextView = (TextView) this.view.findViewById(R.id.card_view_title);
        TextView subTitleTextView = (TextView) this.view.findViewById(R.id.card_view_sub_title);
        Button button = (Button) this.view.findViewById(R.id.card_view_button);

        String thisDayOfWeek = daysOfWeek[today.get(Calendar.DAY_OF_WEEK) - 1];
        int thisDayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        String thisMonth = months[today.get(Calendar.MONTH)];
        titleTextView.setText(thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth);

        if (this.calendar.containsKey(this.today)) {
            imageView.setImageResource(R.drawable.open);
            subTitleTextView.setText("Testo da inserire");
            button.setVisibility(View.VISIBLE);
        } else {
            imageView.setImageResource(R.drawable.close);
            subTitleTextView.setText("Chiuso");
            button.setVisibility(View.GONE);
        }
    }

    public void setMonthCalendar(int month, int frameLayoutId) {
        LinkedHashMap<GregorianCalendar, Map<String, String>> monthCalendar = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<GregorianCalendar, Map<String, String>> entry : this.calendar.entrySet()) {
            if (entry.getKey().get(Calendar.MONTH) == month) {
                monthCalendar.put(entry.getKey(), entry.getValue());
            }
        }
        MonthFragment monthFragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt("month", month);
        args.putSerializable("calendar", monthCalendar);
        monthFragment.setArguments(args);
        getFragmentManager().beginTransaction().add(frameLayoutId, monthFragment).commit();
    }
}
