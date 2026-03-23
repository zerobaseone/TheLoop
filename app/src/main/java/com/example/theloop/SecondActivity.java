package com.example.theloop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private EventRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        repository = new EventRepository(this);

        recyclerView = findViewById(R.id.recyclerView);
        // stack events vertically in a scrollable list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with event list and a delete callback.
        // when a delete is triggered the repository handles removal and the list refreshes
        eventAdapter = new EventAdapter(repository.getEvents(), event -> {
            repository.deleteEvent(event.getId());
            eventAdapter.updateEvents(repository.getEvents());
        });
        recyclerView.setAdapter(eventAdapter);

        // Highlight the home tab in the bottom navigation bar
        BottomNavMenu.setupBottomNavigation(this, R.id.nav_home);

        Button addNewButton = findViewById(R.id.AddNewButton);
        addNewButton.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, NewEventActivity.class);
            startActivity(intent);
        });
    }

    // refreshes the event list every time this screen becomes visible,
    // so new or edited events show up
    @Override
    protected void onResume() {
        super.onResume();
        eventAdapter.updateEvents(repository.getEvents());
    }
}
