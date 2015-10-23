package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

public class CalendarFragment extends Fragment {

    View view;
    Context context;
    private String fileName = "anguriara.ser";
    private Map<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
    final static int YEAR = 2015;
    final static int ANGURIARA_NUMBER_OF_DAYS = 31;
    private Calendar today = new GregorianCalendar(2015, 5, 5);
    private String[] daysOfWeek;
    private String[] months;
    private int[] anguriaraMonths;
    private int[] anguriaraDaysOfMonth;
    private String[] dayEvents;
    private String[] dayEventsDetails;
    private String[] dayFoods;
    private String[] dayOpeningTimes;
    private String cardViewTitle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean alarmIsSet;
    Calendar alarmTime;
    private Intent notificationIntent;
    private PendingIntent notificationPendingIntent;
    private AlarmManager notificationAlarmManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_calendar, container, false);
        this.context = view.getContext();

        MainActivity.toolbar.setTitle(getResources().getString(R.string.calendar));

        this.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        this.months = getResources().getStringArray(R.array.months);
        this.anguriaraMonths = getResources().getIntArray(R.array.anguriara_months);
        this.anguriaraDaysOfMonth = getResources().getIntArray(R.array.anguriara_days_of_month);
        this.dayEvents = getResources().getStringArray(R.array.day_events);
        this.dayEventsDetails = getResources().getStringArray(R.array.day_event_details);
        this.dayFoods = getResources().getStringArray(R.array.day_foods);
        this.dayOpeningTimes = getResources().getStringArray(R.array.day_opening_time);


        File file = new File(this.context.getFilesDir(), this.fileName);
//        if (file.exists()) {
//            this.calendar = deserializeCalendar();
//        } else {
            this.calendar = setCalendar();
            serializeCalendar(this.calendar);
//        }

        thisDay();
//      Calendars Fragment maybe have some problems. Move from portrait to landscape add many fragments.
        setMonthCalendar(Calendar.JUNE, R.id.june_calendar);
        setMonthCalendar(Calendar.JULY, R.id.july_calendar);

        this.sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        alarmIsSet = false;
        alarmIsSet = sharedPreferences.getBoolean("alarm", alarmIsSet);

        if (! alarmIsSet)
            setAlarm();
//        cancelAlarm();

        return this.view;
    }

    public Map<GregorianCalendar, LinkedHashMap<String, String>> setCalendar() {
        Map<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
        for (int i = 0; i < ANGURIARA_NUMBER_OF_DAYS; i++) {
            LinkedHashMap<String, String> eveningMap = new LinkedHashMap<>();
            eveningMap.put("event", dayEvents[i]);
            eveningMap.put("event_details", dayEventsDetails[i]);
            eveningMap.put("food", dayFoods[i]);
            eveningMap.put("openingTime", dayOpeningTimes[i]);
            calendar.put(new GregorianCalendar(YEAR, anguriaraMonths[i], anguriaraDaysOfMonth[i]), eveningMap);
        }
        return calendar;
    }

    public void serializeCalendar(Map<GregorianCalendar, LinkedHashMap<String, String>> calendar) {
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

    public Map<GregorianCalendar, LinkedHashMap<String, String>> deserializeCalendar(){
        Map<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
        try {
            File file = new File(this.context.getFilesDir(), this.fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            calendar = (LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>>) objectInputStream.readObject();
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
        this.cardViewTitle = thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth;
        titleTextView.setText(this.cardViewTitle);

        if (this.calendar.containsKey(this.today)) {
            imageView.setImageResource(R.drawable.open);
            String dayEventAndFood = getResources().getString(R.string.event) + ": " + calendar.get(today).get("event") + "\n" + getResources().getString(R.string.food) + ": " + calendar.get(today).get("food");
            subTitleTextView.setText(dayEventAndFood);

            final Bundle dayArgs = new Bundle();
            dayArgs.putSerializable("date", today);
            dayArgs.putSerializable("day", calendar.get(today));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.toolbar.setTitle(cardViewTitle);
                    DayFragment dayFragment = new DayFragment();
                    dayFragment.setArguments(dayArgs);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, dayFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            button.setVisibility(View.VISIBLE);
        } else {
            imageView.setImageResource(R.drawable.close);
            subTitleTextView.setText(getResources().getString(R.string.close));
            button.setVisibility(View.GONE);
        }

    }

    public void setMonthCalendar(int month, int frameLayoutId) {
        LinkedHashMap<GregorianCalendar, Map<String, String>> monthCalendar = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : this.calendar.entrySet()) {
            if (entry.getKey().get(Calendar.MONTH) == month) {
                monthCalendar.put(entry.getKey(), entry.getValue());
            }
        }
        MonthFragment monthFragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt("month", month);
        args.putSerializable("calendar", monthCalendar);
        monthFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(frameLayoutId, monthFragment).commit();
    }

    public void setAlarm() {
        for (int i = 0; i < 10; i++) {
            this.notificationIntent = new Intent(getContext(), MyNotification.class);
            this.notificationIntent.putExtra("notification_text", "BlaBla");
            this.notificationPendingIntent = PendingIntent.getBroadcast(getContext(), i, this.notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            this.notificationAlarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            this.alarmTime = Calendar.getInstance();
            this.alarmTime.setTimeInMillis(System.currentTimeMillis());
            this.alarmTime.set(Calendar.HOUR_OF_DAY, 16);
            this.alarmTime.set(Calendar.MINUTE, 20 + i);
            this.alarmTime.set(Calendar.SECOND, 0);
            if (! (this.alarmTime.getTimeInMillis() < System.currentTimeMillis())) {
                this.notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, this.alarmTime.getTimeInMillis(), this.notificationPendingIntent);
            }
        }
        alarmIsSet = true;
        this.editor = sharedPreferences.edit();
        editor.putBoolean("alarm", alarmIsSet);
        editor.apply();
    }

    public void cancelAlarm() {
        this.notificationAlarmManager.cancel(this.notificationPendingIntent);
        this.alarmIsSet = false;
        this.editor = sharedPreferences.edit();
        editor.putBoolean("alarm", alarmIsSet);
        editor.apply();
    }
}