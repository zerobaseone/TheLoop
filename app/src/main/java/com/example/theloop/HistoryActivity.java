package com.example.theloop;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Set up shared bottom nav logic
        BottomNavMenu.setupBottomNavigation(this, R.id.nav_history);
    }
}
