package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Luca on 23/10/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    Calendar alarmTime;
    private Intent notificationIntent;
    private PendingIntent notificationPendingIntent;
    private AlarmManager notificationAlarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            for (int i = 0; i < 5; i++) {
                this.notificationIntent = new Intent(context, MyNotification.class);
                this.notificationIntent.putExtra("notification_text", "BlaBla");
                this.notificationPendingIntent = PendingIntent.getBroadcast(context, i, this.notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                this.notificationAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                this.alarmTime = Calendar.getInstance();
                this.alarmTime.setTimeInMillis(System.currentTimeMillis());
                this.alarmTime.set(Calendar.HOUR_OF_DAY, 20);
                this.alarmTime.set(Calendar.MINUTE, 50 + i);
                this.alarmTime.set(Calendar.SECOND, 0);
                if (!(this.alarmTime.getTimeInMillis() < System.currentTimeMillis())) {
                    this.notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, this.alarmTime.getTimeInMillis(), this.notificationPendingIntent);
                }
            }
        }
    }
}
