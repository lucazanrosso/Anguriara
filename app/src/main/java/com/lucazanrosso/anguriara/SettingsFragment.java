package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class SettingsFragment extends Fragment {

    View view;

    private SwitchCompat notificationSwitch;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean alarmIsSet;
    private PendingIntent notificationPendingIntent;
    private AlarmManager notificationAlarmManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        this.notificationSwitch = (SwitchCompat) view.findViewById(R.id.notifications_switch);

//        boolean alarmIsSet = true;
//        this.sharedPreferences.getBoolean("alarmIsSet", alarmIsSet);
//        if (alarmIsSet) {
//            notificationSwitch.setChecked(true);
//            Log.d("succede2", "succede2");
//        }
//        else {
//            notificationSwitch.setChecked(false);
//            Log.d("succede3", "succede3");
//        }

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setAlarm();
                } else {
                    setAlarm();
                }
            }
        });
        return view;
    }

    public void setAlarm() {
        int i = 0;
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
            String notificationText;
            if (!entry.getValue().get("event").isEmpty()) {
                notificationText = entry.getValue().get("event");
                if (!entry.getValue().get("food").isEmpty())
                    notificationText += ", " + entry.getValue().get("food");
                notificationText += " " + getResources().getString(R.string.and_much_more);
            } else
                notificationText = getResources().getString(R.string.open);
            Intent notificationIntent = new Intent(getContext(), MyNotification.class);
            notificationIntent.putExtra("notification_text", notificationText);
            this.notificationPendingIntent = PendingIntent.getBroadcast(getContext(), i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            this.notificationAlarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

            if (notificationSwitch.isChecked()) {
                Calendar alarmTime = Calendar.getInstance();
                alarmTime.setTimeInMillis(System.currentTimeMillis());
//           alarmTime.set(CalendarFragment.YEAR, entry.getKey().get(Calendar.MONTH), entry.getKey().get(Calendar.DAY_OF_MONTH), 17, 0);
                //Test
                alarmTime.set(2015, 10, 25, 23, i);
                if (!(alarmTime.getTimeInMillis() < System.currentTimeMillis()))
                    this.notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), this.notificationPendingIntent);
                this.editor = sharedPreferences.edit();
                editor.putBoolean("alarmIsSet", true);
                editor.apply();
            } else {
                this.notificationAlarmManager.cancel(this.notificationPendingIntent);
                this.editor = sharedPreferences.edit();
                editor.putBoolean("alarmIsSet", false);
                editor.apply();
            }
            i++;
        }

        ComponentName receiver = new ComponentName(getContext(), BootReceiver.class);
        PackageManager pm = getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
