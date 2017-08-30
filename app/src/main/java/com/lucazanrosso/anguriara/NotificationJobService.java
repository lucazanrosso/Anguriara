package com.lucazanrosso.anguriara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotificationJobService extends JobService {

    SharedPreferences sharedPreferences;

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("test");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (sharedPreferences.getBoolean("firebaseAlarmIsSet", true)) {
                    int currentNotificationId = dataSnapshot.child("id").getValue(Integer.class);
                    int day = dataSnapshot.child("day").getValue(Integer.class);
                    int month = dataSnapshot.child("month").getValue(Integer.class);
                    int year = dataSnapshot.child("year").getValue(Integer.class);
                    boolean badWeather = dataSnapshot.child("bad_weather").getValue(Boolean.class);
                    Calendar notificationDay = new GregorianCalendar(year, month, day);
                    Calendar todayInstance = new GregorianCalendar();
//                        Calendar todayInstance = new GregorianCalendar(2017, 5, 10);
                    Calendar today = new GregorianCalendar(todayInstance.get(Calendar.YEAR), todayInstance.get(Calendar.MONTH), todayInstance.get(Calendar.DAY_OF_MONTH));
                    int localNotificationId = sharedPreferences.getInt("notificationId", 0);
                    if (localNotificationId < currentNotificationId && today.equals(notificationDay)) {
                        Intent notificationIntent = new Intent(getApplicationContext(), MyNotification.class);
                        if (badWeather) {
                            MainActivity.badDay = notificationDay;
                            sharedPreferences.edit().putInt("BadWeatherYear", year).apply();
                            sharedPreferences.edit().putInt("BadWeatherMonth", month).apply();
                            sharedPreferences.edit().putInt("BadWeatherDay", day).apply();
                        } else {
                            MainActivity.badDay = null;
                            sharedPreferences.edit().putInt("BadWeatherYear", 0).apply();
                            sharedPreferences.edit().putInt("BadWeatherMonth", 0).apply();
                            sharedPreferences.edit().putInt("BadWeatherDay", 0).apply();
                        }
                        notificationIntent.putExtra("notification_title", dataSnapshot.child("title").getValue(String.class));
                        notificationIntent.putExtra("notification_text", dataSnapshot.child("text").getValue(String.class));
                        getApplicationContext().sendBroadcast(notificationIntent);
                    }
                    sharedPreferences.edit().putInt("notificationId", currentNotificationId).apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }

}
