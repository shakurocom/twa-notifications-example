package com.shakuro.twa_demo_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmService extends FirebaseMessagingService {

    public static final String CUSTOM_NOTIFICATION_CHANNEL = "CustomNotificationsChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationsChannels();
    }

    private NotificationCompat.Builder initNotification(String channel, String title, String body) {
        return new NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
    }

    private void crateNotification(RemoteMessage remoteMessage) {
        Integer id = 0 + (int)(Math.random() * 2147483647);

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String url = remoteMessage.getData().get("url");
        String action_name = remoteMessage.getData().get("action_name");
        String action_url = remoteMessage.getData().get("action_url");

        NotificationCompat.Builder notificationBuilder = this.initNotification(CUSTOM_NOTIFICATION_CHANNEL, title, body);

        Intent notificationIntent = new Intent(this, LauncherActivity.class);
        notificationIntent.setData(Uri.parse(url));
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, id, notificationIntent, 0);

        Intent actionIntent = new Intent(this, LauncherActivity.class);
        actionIntent.setData(Uri.parse(action_url));
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, id, actionIntent, 0);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, action_name, actionPendingIntent).build();

        Notification notification = notificationBuilder
                .setContentIntent(notificationPendingIntent)
                .addAction(action)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("DEBUG", remoteMessage.getData().toString());
            if (!remoteMessage.getData().get("payload").isEmpty()) {
                crateNotification(remoteMessage);
            }
        }
    }

    private void createNotificationsChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel customNotificationsChannel = new NotificationChannel(CUSTOM_NOTIFICATION_CHANNEL, "Custom Notifications", NotificationManager.IMPORTANCE_HIGH);
            customNotificationsChannel.setDescription("Custom Notifications Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(customNotificationsChannel);
        }
    }

    // Almost always, this event occurs after installing the application, at the time of the first launch of the app.
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        String previousToken = getApplicationContext()
                .getSharedPreferences("_", MODE_PRIVATE)
                .getString("fb", "empty");

        // We check if we have a current token in the store.
        if (previousToken != token) {

            // Installing a new token in the storage
            getSharedPreferences("_", MODE_PRIVATE)
                    .edit()
                    .putString("fb", token)
                    .apply();

            Intent intent = new Intent(this, LauncherActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // This command restarts the application to update or set a new token
            this.startActivity(intent);
        }
    }
}