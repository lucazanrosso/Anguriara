package com.lucazanrosso.anguriara;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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
                    .setContentText(this.notificationText);
            Intent resultIntent = new Intent(context, MainActivity.class);
            //resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
    }
}
