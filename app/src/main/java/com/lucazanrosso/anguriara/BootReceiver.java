package com.lucazanrosso.anguriara;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar = MainActivity.deserializeCalendar(context);
        MainActivity.setAlarm(context, calendar, sharedPreferences.getBoolean("alarmIsSet", false), true);
    }
}
