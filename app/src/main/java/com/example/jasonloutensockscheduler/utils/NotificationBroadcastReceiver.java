package com.example.jasonloutensockscheduler.utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.jasonloutensockscheduler.R;

import static androidx.core.content.ContextCompat.getSystemService;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    static int notificationId;
    String channelId = "test";


    @Override
    public void onReceive(Context context, Intent intent) {

        createNotificationChannel(context, channelId);

        //build notification
        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_priority_high)
                .setContentText(intent.getStringExtra("key"))
                .setContentTitle("Reminder!").build();

        //handle notification
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notificationId++, notification);
    }


    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "notification channel";
            String desc = "notification channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }


    }
}
