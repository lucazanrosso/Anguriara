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

    int notificationId;
    String notificationTitle;
    String notificationText;
    int notificationIcon;
    final static String GROUP_NOTIFICATION = "group_notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.notificationId = intent.getIntExtra("id", 0);
        this.notificationTitle = context.getResources().getString(R.string.this_evening);
        this.notificationText = intent.getStringExtra("notification_text");
        this.notificationIcon = R.drawable.notification;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(this.notificationIcon)
                .setContentTitle(this.notificationTitle)
                .setContentText(this.notificationText)
                .setGroup(GROUP_NOTIFICATION)
                .setGroupSummary(true)
                .setColor(ContextCompat.getColor(context, R.color.accent))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        Intent resultIntent = new Intent(context, MainActivity.class);
        //resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat mNotificationManagerCompat =
                NotificationManagerCompat.from(context);
        mNotificationManagerCompat.notify(notificationId, mBuilder.build());
    }
}