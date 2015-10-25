package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar = deserializeCalendar(context);
            int i = 0;
            for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : calendar.entrySet()) {
                String notificationText;
                if (! entry.getValue().get("event").isEmpty()) {
                    notificationText = entry.getValue().get("event");
                    if (!entry.getValue().get("food").isEmpty())
                        notificationText += ", " + entry.getValue().get("food");
                    notificationText +=  " " + context.getResources().getString(R.string.and_much_more);
                } else
                    notificationText = context.getResources().getString(R.string.open);
                Intent notificationIntent = new Intent(context, MyNotification.class);
                notificationIntent.putExtra("notification_text", notificationText);
                PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(context, i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager notificationAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar alarmTime = Calendar.getInstance();
                alarmTime.setTimeInMillis(System.currentTimeMillis());
//                alarmTime.set(CalendarFragment.YEAR, entry.getKey().get(Calendar.MONTH), entry.getKey().get(Calendar.DAY_OF_MONTH), 17, 0);
                //Test
                alarmTime.set(2015, 9, 25, 20, i);
                if (!(alarmTime.getTimeInMillis() < System.currentTimeMillis())) {
                    notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), notificationPendingIntent);
                }
                i++;
            }
        }
    }

    public LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> deserializeCalendar(Context context){
        String fileName = "anguriara.ser";
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
        try {
            File file = new File(context.getFilesDir(), fileName);
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
