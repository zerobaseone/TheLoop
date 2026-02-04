package com.example.theloop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    EventDatabase db;   // our SQLite database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialize the database
        db = new EventDatabase(this);

        // load events from the database
        ArrayList<Event> events = loadEventsFromDb();

        // connect the adapter with data from DB
        eventAdapter = new EventAdapter(events, db);
        recyclerView.setAdapter(eventAdapter);

        BottomNavMenu.setupBottomNavigation(this, R.id.nav_home);

        // links 'Add New...' button to NewEventActivity
        Button addNewButton = findViewById(R.id.AddNewButton);
        addNewButton.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, NewEventActivity.class);
            startActivity(intent);
        });

    }

    private ArrayList<Event> loadEventsFromDb() {
        ArrayList<Event> events = new ArrayList<>();
        Cursor cursor = db.getAllEvents();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_ID));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_ARTIST));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_LOCATION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_DATE));

                events.add(new Event(id, artist, location, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return events;
    }

    private void refreshEventList() {
        // Reload events from the DB
        ArrayList<Event> events = loadEventsFromDb();

        // Update adapter data
        eventAdapter.updateEvents(events);
    }

    // makes new events appear in the recyclerview after returning from new event activity
    @Override
    protected void onResume() {
        super.onResume();
        refreshEventList();
    }


}
