package com.lucazanrosso.anguriara;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.LinkedHashMap;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        LinkedHashMap<Calendar, LinkedHashMap<String, Object>> calendar = MainActivity.deserializeCalendar(context);
        MainActivity.setAlarm(context, calendar, sharedPreferences.getBoolean("alarmIsSet", false), true);
    }
}