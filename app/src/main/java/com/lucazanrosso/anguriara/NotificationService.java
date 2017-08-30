package com.lucazanrosso.anguriara;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotificationService extends Service {

    private ServiceHandler mServiceHandler;

    SharedPreferences sharedPreferences;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        Context context;

        private ServiceHandler(Looper looper, Context context) {
            super(looper);
            this.context = context;
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                // Restore interrupt status.
//                Thread.currentThread().interrupt();
//            }
//            // Stop the service using the startId, so that we don't stop
//            // the service in the middle of handling another job
//            stopSelf(msg.arg1);

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
                            Intent notificationIntent = new Intent(context, MyNotification.class);
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
                            context.sendBroadcast(notificationIntent);
                        }
                        sharedPreferences.edit().putInt("notificationId", currentNotificationId).apply();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onCreate() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        Notification notification = new NotificationCompat.Builder(this, "")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("My Awesome App")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        Looper mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}