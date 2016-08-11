package com.lucazanrosso.anguriara;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

public class MyNotification extends BroadcastReceiver{

    String notificationTitle;
    String notificationText;
    int notificationIcon;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.notificationTitle = context.getResources().getString(R.string.this_evening);
        this.notificationText = intent.getStringExtra("notification_text");
        this.notificationIcon = R.drawable.notification;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(this.notificationIcon)
                .setContentTitle(this.notificationTitle)
                .setContentText(this.notificationText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                .setColor(ContextCompat.getColor(context, R.color.accent))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat mNotificationManagerCompat =
                NotificationManagerCompat.from(context);
        mNotificationManagerCompat.notify(0, mBuilder.build());
    }
}