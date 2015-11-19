package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CalendarFragment extends Fragment {

    View view;
    Context context;

    private Calendar today = new GregorianCalendar(2015, 5, 5);
    private String[] daysOfWeek;
    private String[] months;

    private String cardViewTitle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean alarmIsSet;
    private PendingIntent notificationPendingIntent;
    private AlarmManager notificationAlarmManager;

//    private Calendar date;
//    private String toolbarTitle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_calendar, container, false);
        this.context = view.getContext();

        MainActivity.toolbar.setTitle(getResources().getString(R.string.calendar));

        this.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        this.months = getResources().getStringArray(R.array.months);

        thisDay();
        setMonthCalendar(Calendar.JUNE, R.id.june_calendar);
        setMonthCalendar(Calendar.JULY, R.id.july_calendar);

        this.sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        alarmIsSet = false;
        alarmIsSet = sharedPreferences.getBoolean("alarm", alarmIsSet);
        if (! alarmIsSet)
            setAlarm();

        return this.view;
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

        if (MainActivity.calendar.containsKey(this.today)) {
            imageView.setImageResource(R.drawable.open);
            String dayEventAndFood = getResources().getString(R.string.event) + ": " +
                    MainActivity.calendar.get(today).get("event") + "\n" +
                    getResources().getString(R.string.food) + ": " +
                    MainActivity.calendar.get(today).get("food");
            subTitleTextView.setText(dayEventAndFood);

            final Bundle dayArgs = new Bundle();
            dayArgs.putSerializable("date", today);
            dayArgs.putSerializable("day", MainActivity.calendar.get(today));
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
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> monthCalendar = new LinkedHashMap<>();
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
            if (entry.getKey().get(Calendar.MONTH) == month) {
                monthCalendar.put(entry.getKey(), entry.getValue());
            }
        }
        MonthFragment monthFragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt("month", month);
        args.putSerializable("calendar", monthCalendar);
        monthFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(frameLayoutId, monthFragment).commit();
//        this.date = new GregorianCalendar(2015, month, 1);
//
//        LinearLayout calendarLinearLayout = (LinearLayout) view.findViewById(R.id.calendar_layout);
//        LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams dayLayoutParams = new LinearLayout.LayoutParams(
//                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
//        TextView textView = (TextView) view.findViewById(R.id.month);
//
//        Iterator iterator = monthCalendar.entrySet().iterator();
//        if (iterator.hasNext()) {
//            Map.Entry<GregorianCalendar, Map<String, String>> entry = (Map.Entry<GregorianCalendar, Map<String, String>>) iterator.next();
//
//            textView.setText("Bla");
//            for (int i = 1; i <= this.date.get(Calendar.DAY_OF_MONTH); i++) {
//                LinearLayout weekLinearLayout = new LinearLayout(getActivity());
//                weekLinearLayout.setLayoutParams(weekLayoutParams);
//                for (int j = 0; j <= 6; j++) {
//                    Button button = new Button(getActivity());
//                    button.setLayoutParams(dayLayoutParams);
//                    button.setBackgroundResource(0);
//                    if (((this.date.get(Calendar.DAY_OF_WEEK) + 5) % 7) == j && this.date.get(Calendar.MONTH) == month) {
//                        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                        button.setText(Integer.toString(this.date.get(Calendar.DAY_OF_MONTH)));
//                        if (entry.getKey().get(Calendar.DAY_OF_YEAR) == this.date.get(Calendar.DAY_OF_YEAR)) {
//                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
//                            button.setTypeface(null, Typeface.BOLD);
//
//                            String thisDayOfWeek = daysOfWeek[date.get(Calendar.DAY_OF_WEEK) - 1];
//                            int thisDayOfMonth = date.get(Calendar.DAY_OF_MONTH);
//                            String thisMonth = months[date.get(Calendar.MONTH)];
//                            this.toolbarTitle = thisDayOfWeek + " " + thisDayOfMonth + " " + thisMonth;
//
//                            final Bundle dayArgs = new Bundle();
//                            dayArgs.putSerializable("date", date);
//                            dayArgs.putSerializable("day", monthCalendar.get(date));
//                            button.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    MainActivity.toolbar.setTitle(toolbarTitle);
//                                    DayFragment dayFragment = new DayFragment();
//                                    dayFragment.setArguments(dayArgs);
//                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                    transaction.replace(R.id.frame_container, dayFragment);
//                                    transaction.addToBackStack(null);
//                                    transaction.commit();
//                                }
//                            });
//                            if (iterator.hasNext()) {
//                                entry = (Map.Entry<GregorianCalendar, Map<String, String>>) iterator.next();
//                            }
//                        } else {
//                            button.setEnabled(false);
//                            button.setTextColor(ContextCompat.getColor(getContext(), R.color.disabled_text));
//                        }
//                        this.date.add(Calendar.DAY_OF_YEAR, 1);
//                    }
//                    weekLinearLayout.addView(button);
//                }
//                calendarLinearLayout.addView(weekLinearLayout);
//            }
//        }
    }

    public void setAlarm() {
        int i = 0;
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
            String notificationText;
            if (! entry.getValue().get("event").isEmpty()) {
                notificationText = entry.getValue().get("event");
                if (!entry.getValue().get("food").isEmpty())
                    notificationText += ", " + entry.getValue().get("food");
                notificationText +=  " " + getResources().getString(R.string.and_much_more);
            } else
                notificationText = getResources().getString(R.string.open);
            Intent notificationIntent = new Intent(getContext(), MyNotification.class);
            notificationIntent.putExtra("notification_text", notificationText);
            this.notificationPendingIntent = PendingIntent.getBroadcast(getContext(), i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            this.notificationAlarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Calendar alarmTime = Calendar.getInstance();
            alarmTime.setTimeInMillis(System.currentTimeMillis());
//           alarmTime.set(CalendarFragment.YEAR, entry.getKey().get(Calendar.MONTH), entry.getKey().get(Calendar.DAY_OF_MONTH), 17, 0);
            //Test
            alarmTime.set(2015, 9, 25, 21, i + 10);
            if (! (alarmTime.getTimeInMillis() < System.currentTimeMillis()))
                this.notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), this.notificationPendingIntent);
            i++;
        }
        alarmIsSet = true;
        this.editor = sharedPreferences.edit();
        editor.putBoolean("alarm", alarmIsSet);
        editor.apply();

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm() {
        this.notificationAlarmManager.cancel(this.notificationPendingIntent);
        this.alarmIsSet = false;
        this.editor = sharedPreferences.edit();
        editor.putBoolean("alarm", alarmIsSet);
        editor.apply();

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}