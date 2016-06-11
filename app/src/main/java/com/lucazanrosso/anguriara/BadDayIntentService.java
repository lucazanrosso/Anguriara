package com.lucazanrosso.anguriara;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.GregorianCalendar;


public class BadDayIntentService extends IntentService {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("test");
    Context context = BadDayIntentService.this;

    public BadDayIntentService() {
        super("BadDayIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                        .setSmallIcon(R.drawable.notification)
//                        .setContentTitle(context.getResources().getString(R.string.this_evening))
//                        .setContentText(context.getResources().getString(R.string.bad_weather))
//                        .setColor(ContextCompat.getColor(context, R.color.accent));
//                Intent resultIntent = new Intent(context, MainActivity.class);
//                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                mBuilder.setContentIntent(resultPendingIntent);
//                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//                mBuilder.setAutoCancel(true);
//                NotificationManager mNotificationManager =
//                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                mNotificationManager.notify(1, mBuilder.build());
                Intent notificationIntent = new Intent(context, MyNotification.class);
                notificationIntent.putExtra("notification_text", context.getResources().getString(R.string.bad_weather));
                context.sendBroadcast(notificationIntent);

                boolean isBadDay = dataSnapshot.child("bad_weather").getValue(Boolean.class);
                if (isBadDay) {
                    MainActivity.badDay = new GregorianCalendar(dataSnapshot.child("year").getValue(Integer.class), dataSnapshot.child("month").getValue(Integer.class), dataSnapshot.child("day").getValue(Integer.class));
                    MainActivity.serializeBadDay(context);
                    if (!MainActivity.today.equals(MainActivity.badDay)) {
                        MainActivity.badDay = null;
                        new File(context.getFilesDir(), "bad_day.ser").delete();
                    } else {

                    }
                } else if (MainActivity.badDay != null) {
                    MainActivity.badDay = null;
                    new File(context.getFilesDir(), "bad_day.ser").delete();
                }
                CalendarFragment.thisDayText.setText(CalendarFragment.setDateText(MainActivity.today, context));
                CalendarFragment.thisDayImage.setImageResource(CalendarFragment.setThisDayImage(MainActivity.today));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
