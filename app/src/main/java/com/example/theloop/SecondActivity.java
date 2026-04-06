package com.example.theloop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SecondActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private EventRepository repository;
    private Button loadMoreButton;
    private final ArrayList<Event> allEvents = new ArrayList<>();
    private Set<String> starredIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        repository = new EventRepository();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NotificationScheduler.createNotificationChannel(this);

        eventAdapter = new EventAdapter(
                allEvents,
                starredIds,
                // Star click: toggle star/unstar, schedule/cancel notification
                (event, isCurrentlyStarred) -> {
                    if (isCurrentlyStarred) {
                        repository.unstarEvent(event.getId());
                        starredIds.remove(event.getId());
                        NotificationScheduler.cancel(this, event.getId());
                    } else {
                        repository.starEvent(event);
                        starredIds.add(event.getId());
                        NotificationScheduler.schedule(this, event);
                    }
                    eventAdapter.updateStarredIds(new HashSet<>(starredIds));
                },
                null // no delete button on the home screen
        );
        recyclerView.setAdapter(eventAdapter);

        loadMoreButton = findViewById(R.id.loadMoreButton);
        loadMoreButton.setVisibility(View.GONE);
        loadMoreButton.setOnClickListener(v -> loadMore());

        BottomNavMenu.setupBottomNavigation(this, R.id.nav_home);

        Button addNewButton = findViewById(R.id.AddNewButton);
        addNewButton.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, NewEventActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }

    // Loads starred IDs first, then the first page of events
    private void loadEvents() {
        allEvents.clear();
        eventAdapter.updateEvents(new ArrayList<>(allEvents));
        loadMoreButton.setVisibility(View.GONE);

        repository.getStarredEventIds(new EventRepository.StarredIdsCallback() {
            @Override
            public void onSuccess(Set<String> ids) {
                starredIds = ids;
                eventAdapter.updateStarredIds(new HashSet<>(starredIds));
                loadFirstPage();
            }

            @Override
            public void onError(Exception e) {
                starredIds = new HashSet<>();
                loadFirstPage();
            }
        });
    }

    private void loadFirstPage() {
        repository.getEvents(new EventRepository.EventsCallback() {
            @Override
            public void onSuccess(ArrayList<Event> events) {
                allEvents.addAll(events);
                eventAdapter.updateEvents(new ArrayList<>(allEvents));
                loadMoreButton.setVisibility(events.size() == 20 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SecondActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMore() {
        repository.loadMore(new EventRepository.EventsCallback() {
            @Override
            public void onSuccess(ArrayList<Event> events) {
                allEvents.addAll(events);
                eventAdapter.updateEvents(new ArrayList<>(allEvents));
                loadMoreButton.setVisibility(events.size() == 20 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SecondActivity.this, "Failed to load more events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
