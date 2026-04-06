package com.example.theloop;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private EventRepository repository;
    private final ArrayList<Event> starredEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        repository = new EventRepository();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // All events shown here are starred — pass a full set of their IDs so stars show filled
        // Star click on My Events = unstar, then remove from list
        // No delete button (null deleteListener)
        eventAdapter = new EventAdapter(
                starredEvents,
                new HashSet<>(),
                (event, isCurrentlyStarred) -> {
                    repository.unstarEvent(event.getId());
                    NotificationScheduler.cancel(this, event.getId());
                    starredEvents.remove(event);
                    eventAdapter.updateEvents(new ArrayList<>(starredEvents));
                },
                null // no delete button on My Events screen
        );
        recyclerView.setAdapter(eventAdapter);

        BottomNavMenu.setupBottomNavigation(this, R.id.nav_history);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStarredEvents();
    }

    private void loadStarredEvents() {
        starredEvents.clear();

        repository.getStarredEvents(new EventRepository.EventsCallback() {
            @Override
            public void onSuccess(ArrayList<Event> events) {
                starredEvents.addAll(events);
                // All events here are starred — build the full ID set for the adapter
                HashSet<String> ids = new HashSet<>();
                for (Event e : starredEvents) ids.add(e.getId());
                eventAdapter.updateStarredIds(ids);
                eventAdapter.updateEvents(new ArrayList<>(starredEvents));
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(HistoryActivity.this, "Failed to load saved events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
