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

//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
//    private boolean alarmIsSet;
//    private PendingIntent notificationPendingIntent;
//    private AlarmManager notificationAlarmManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        SwitchCompat notificationSwitch = (SwitchCompat) view.findViewById(R.id.notifications_switch);

        boolean alarmIsSet = true;
        MainActivity.sharedPreferences.getBoolean("alarmIsSet", alarmIsSet);
        if (alarmIsSet) {
            notificationSwitch.setChecked(true);
            Log.d("succede3", "succede2");
        }
        else {
            notificationSwitch.setChecked(false);
            Log.d("succede3", "succede3");
        }

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.setAlarm(getContext());
                } else {
                    MainActivity.cancelAlarm(getContext());
                }
            }
        });
        return view;
    }

//    public void setAlarm() {
//        int i = 0;
//        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
//            String notificationText;
//            if (!entry.getValue().get("event").isEmpty()) {
//                notificationText = entry.getValue().get("event");
//                if (!entry.getValue().get("food").isEmpty())
//                    notificationText += ", " + entry.getValue().get("food");
//                notificationText += " " + getResources().getString(R.string.and_much_more);
//            } else
//                notificationText = getResources().getString(R.string.open);
//            Intent notificationIntent = new Intent(getContext(), MyNotification.class);
//            notificationIntent.putExtra("notification_text", notificationText);
//            this.notificationPendingIntent = PendingIntent.getBroadcast(getContext(), i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            this.notificationAlarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//            Calendar alarmTime = Calendar.getInstance();
//            alarmTime.setTimeInMillis(System.currentTimeMillis());
////           alarmTime.set(CalendarFragment.YEAR, entry.getKey().get(Calendar.MONTH), entry.getKey().get(Calendar.DAY_OF_MONTH), 17, 0);
//            //Test
//            alarmTime.set(2015, 11, 25, 20, i + 20);
//            if (!(alarmTime.getTimeInMillis() < System.currentTimeMillis()))
//                this.notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), this.notificationPendingIntent);
//            i++;
//        }
//        alarmIsSet = true;
//        this.editor = sharedPreferences.edit();
//        editor.putBoolean("alarm", alarmIsSet);
//        editor.apply();
//
//        ComponentName receiver = new ComponentName(getContext(), BootReceiver.class);
//        PackageManager pm = getContext().getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//    }
//

}
