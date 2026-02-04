package com.example.theloop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // get username from SharedPreferences
        String username = getSharedPreferences("TheLoopPrefs", MODE_PRIVATE)
                .getString("loggedInUser", "Unknown");

        // put the username in TextView
        TextView usernameTextView = findViewById(R.id.textView2);
        usernameTextView.setText("Username: " + username);

        //  logout
        Button logoutButton = findViewById(R.id.LogOutButton);
        logoutButton.setOnClickListener(v -> {
            // clear the logged-in user
            getSharedPreferences("TheLoopPrefs", MODE_PRIVATE)
                    .edit()
                    .remove("loggedInUser")
                    .apply();

            // return to login screen
            Intent intent = new Intent(UserActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // prevents back navigation
            startActivity(intent);
            finish();
        });

        // Setup BottomNavigationView
        BottomNavMenu.setupBottomNavigation(this, R.id.nav_user);
    }
}
