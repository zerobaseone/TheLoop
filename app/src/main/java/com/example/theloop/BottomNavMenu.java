package com.example.theloop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavMenu {

    public static void setupBottomNavigation(final Activity activity, int selectedItemId) {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(selectedItemId);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == selectedItemId) {
                return true; // if already on this screen
            }

            Intent intent = null;

            if (itemId == R.id.nav_home) {
                intent = new Intent(activity, SecondActivity.class);
            } else if (itemId == R.id.nav_history) {
                intent = new Intent(activity, HistoryActivity.class);
            } else if (itemId == R.id.nav_notifications) {
                intent = new Intent(activity, NotificationsActivity.class);
            } else if (itemId == R.id.nav_user) {
                // Gets username
                SharedPreferences prefs = activity.getSharedPreferences("TheLoopPrefs", Activity.MODE_PRIVATE);
                String username = prefs.getString("loggedInUser", "Unknown");

                intent = new Intent(activity, UserActivity.class);
            }

            if (intent != null) {
                activity.startActivity(intent);
            }

            return true;
        });
    }
}
