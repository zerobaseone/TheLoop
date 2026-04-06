package com.example.theloop;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class NotificationsActivity extends AppCompatActivity {

    private Switch notifSwitch;
    private SharedPreferences prefs;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    checkExactAlarmPermission();
                } else {
                    // Permission denied — turn switch back off
                    notifSwitch.setChecked(false);
                    Toast.makeText(this,
                            "Notification permission denied. Enable it in app settings.",
                            Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        prefs = getSharedPreferences(NotificationScheduler.PREFS_NAME, MODE_PRIVATE);
        notifSwitch = findViewById(R.id.switch1);

        // Restore saved preference
        notifSwitch.setChecked(prefs.getBoolean(NotificationScheduler.NOTIF_ENABLED_KEY, true));

        notifSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                requestNotificationPermissionIfNeeded();
            } else {
                prefs.edit().putBoolean(NotificationScheduler.NOTIF_ENABLED_KEY, false).apply();
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavMenu.setupBottomNavigation(this, R.id.nav_notifications);
    }

    private void requestNotificationPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            // Already granted — check exact alarm permission
            checkExactAlarmPermission();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private void checkExactAlarmPermission() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (!alarmManager.canScheduleExactAlarms()) {
            // Send user to the exact alarm settings screen
            Toast.makeText(this,
                    "Please allow The Loop to schedule exact alarms for event reminders.",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            // All permissions good — enable notifications
            prefs.edit().putBoolean(NotificationScheduler.NOTIF_ENABLED_KEY, true).apply();
            Toast.makeText(this, "Notifications enabled!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recheck exact alarm permission when returning from settings
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        boolean enabled = prefs.getBoolean(NotificationScheduler.NOTIF_ENABLED_KEY, true);
        if (enabled && alarmManager.canScheduleExactAlarms()) {
            prefs.edit().putBoolean(NotificationScheduler.NOTIF_ENABLED_KEY, true).apply();
        }
        notifSwitch.setChecked(prefs.getBoolean(NotificationScheduler.NOTIF_ENABLED_KEY, true));
    }
}
