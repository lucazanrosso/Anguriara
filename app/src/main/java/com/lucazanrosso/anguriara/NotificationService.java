package com.lucazanrosso.anguriara;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotificationService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        Context context;

        public ServiceHandler(Looper looper, Context context) {
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
            editor = sharedPreferences.edit();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("test");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int currentNotificationId = dataSnapshot.child("id").getValue(Integer.class);
                    Calendar notificationDay = new GregorianCalendar(dataSnapshot.child("year").getValue(Integer.class), dataSnapshot.child("month").getValue(Integer.class), dataSnapshot.child("day").getValue(Integer.class));
                    int localNotificationId = sharedPreferences.getInt("notificationId", currentNotificationId);

                    if ((localNotificationId < currentNotificationId || sharedPreferences.getBoolean("firstStartService", true)) && MainActivity.today.equals(notificationDay)) {
                        Intent notificationIntent = new Intent(context, MyNotification.class);

                        boolean isBadDay = dataSnapshot.child("bad_weather").getValue(Boolean.class);
                        if (isBadDay) {
                            MainActivity.badDay = notificationDay;
                            MainActivity.serializeBadDay(context);
                            notificationIntent.putExtra("notification_title", context.getResources().getString(R.string.this_evening));
                            notificationIntent.putExtra("notification_text", context.getResources().getString(R.string.bad_weather));
                        } else {
                            notificationIntent.putExtra("notification_title", dataSnapshot.child("title").getValue(String.class));
                            notificationIntent.putExtra("notification_text", dataSnapshot.child("text").getValue(String.class));
                        }
                        if (sharedPreferences.getBoolean("alarmIsSet", true))
                            context.sendBroadcast(notificationIntent);
//                    CalendarFragment.thisDayText.setText(CalendarFragment.setDateText(MainActivity.today, context));
//                    CalendarFragment.thisDayImage.setImageResource(CalendarFragment.setThisDayImage(MainActivity.today));
                    }
                    editor = sharedPreferences.edit();
                    editor.putInt("notificationId", currentNotificationId).apply();
                    editor.putBoolean("firstStartService", false).apply();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}