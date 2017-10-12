package com.abyx.noticer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Class that creates a new Notification whenever it gets a message from the AlarmManager.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // prepare intent which is triggered if the
        // notification is selected

        Intent temp = new Intent(context, AddActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, temp, 0);

        // build notification
        Notification n  = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_message))
                .setSmallIcon(R.drawable.ic_clippy_white_48dp)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }
}
