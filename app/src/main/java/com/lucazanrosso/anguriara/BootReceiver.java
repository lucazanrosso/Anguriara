package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

/**
 * Created by Luca on 23/10/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    Context context;
    private String fileName = "anguriara.ser";
    private LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar;
    Calendar alarmTime;
    private Intent notificationIntent;
    private PendingIntent notificationPendingIntent;
    private AlarmManager notificationAlarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            this.calendar = (LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>>) intent.getSerializableExtra("calendar");

            this.context = context;
            this.calendar = deserializeCalendar();
            //Log.d("calendar", calendar.get(new GregorianCalendar(2015, 5, 5)).get("event"));
            for (int i = 0; i < 10; i++) {
                this.notificationIntent = new Intent(context, MyNotification.class);
                this.notificationIntent.putExtra("notification_text", calendar.get(new GregorianCalendar(2015, 5, 5)).get("event"));
                this.notificationPendingIntent = PendingIntent.getBroadcast(context, i, this.notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                this.notificationAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                this.alarmTime = Calendar.getInstance();
                this.alarmTime.setTimeInMillis(System.currentTimeMillis());
                this.alarmTime.set(Calendar.HOUR_OF_DAY, 22);
                this.alarmTime.set(Calendar.MINUTE, 45 + i);
                this.alarmTime.set(Calendar.SECOND, 0);
                if (!(this.alarmTime.getTimeInMillis() < System.currentTimeMillis())) {
                    this.notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, this.alarmTime.getTimeInMillis(), this.notificationPendingIntent);
                }
            }
        }
    }
    public LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> deserializeCalendar(){
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
        try {
            File file = new File(context.getFilesDir(), this.fileName);
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
}
