package com.example.theloop;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    public static final String CHANNEL_ID = "theloop_events";
    static final String PREFS_NAME = "TheLoopPrefs";
    static final String NOTIF_ENABLED_KEY = "notificationsEnabled";

    // Creates the notification channel
    public static void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Event Reminders",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Reminders 24 hours before your starred events");
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    public static boolean areNotificationsEnabled(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(NOTIF_ENABLED_KEY, true);
    }

    // schedules a notification for noon the day before the event
    public static void schedule(Context context, Event event) {
        if (!areNotificationsEnabled(context)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (!alarmManager.canScheduleExactAlarms()) return;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date eventDate = sdf.parse(event.getDate());
            if (eventDate == null) return;

            // fires at noon the day before the event
            long notifyTime = eventDate.getTime()
                    + TimeUnit.HOURS.toMillis(12)
                    - TimeUnit.HOURS.toMillis(24);

            // skip if the notification time has already passed
            if (notifyTime <= System.currentTimeMillis()) return;

            Intent intent = new Intent(context, EventNotificationReceiver.class);
            intent.putExtra("artist", event.getArtist());
            intent.putExtra("location", event.getLocation());
            intent.putExtra("eventId", event.getId());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    event.getId().hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // sancels a previously scheduled notification for the given event ID
    public static void cancel(Context context, String eventId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, EventNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                eventId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.cancel(pendingIntent);
    }
}
