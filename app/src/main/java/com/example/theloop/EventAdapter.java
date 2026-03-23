package com.example.theloop;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    // Signals delete requrests to the activity without needing a reference to the database
    public interface OnDeleteClickListener {
        void onDeleteClick(Event event);
    }

    private ArrayList<Event> eventList;
    private final OnDeleteClickListener deleteListener; //handles the actual deletion

    public EventAdapter(ArrayList<Event> events, OnDeleteClickListener deleteListener) {
        this.eventList = events;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    // creates the live View objects
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    // this is called by RecyclerView for each visible row & binds event data to the ui views
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = eventList.get(position);

        holder.artistText.setText(currentEvent.getArtist());
        holder.locationText.setText(currentEvent.getLocation());
        holder.dateText.setText(currentEvent.getDate());

        // Edit Event: click on artist name
        holder.artistText.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditEventActivity.class);
            intent.putExtra("event_id", currentEvent.getId());
            v.getContext().startActivity(intent);
        });

        // Delete event: notify the activity via callback
        holder.deleteButton.setOnClickListener(v -> {
            deleteListener.onDeleteClick(currentEvent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // Caches view references for each row to avoid repeated findViewById calls while scrolling
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView artistText, locationText, dateText;
        Button deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            artistText = itemView.findViewById(R.id.eventArtist);
            locationText = itemView.findViewById(R.id.eventLocation);
            dateText = itemView.findViewById(R.id.eventDate);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // called after add, delete, or edit; replaces the event list and re-renders the RecyclerView
    public void updateEvents(ArrayList<Event> newEvents) {
        this.eventList = newEvents;
        notifyDataSetChanged();
    }
}
