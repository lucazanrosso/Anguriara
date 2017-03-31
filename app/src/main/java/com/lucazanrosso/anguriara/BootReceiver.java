package com.lucazanrosso.anguriara;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        MainActivity.setEveningsAlarm(context, MainActivity.setCalendar(context), sharedPreferences.getBoolean("eveningsAlarmIsSet", false), true);
        MainActivity.setFirebaseAlarm(context, sharedPreferences.getBoolean("firebaseAlarmIsSet", false), true);
    }
}