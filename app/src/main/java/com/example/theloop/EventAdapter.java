package com.example.theloop;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(Event event);
    }

    public interface OnStarClickListener {
        void onStarClick(Event event, boolean isCurrentlyStarred);
    }

    private ArrayList<Event> eventList;
    private Set<String> starredIds;
    private final OnStarClickListener starListener;
    private final OnDeleteClickListener deleteListener; // null = hide delete button

    public EventAdapter(ArrayList<Event> events, Set<String> starredIds,
                        OnStarClickListener starListener, OnDeleteClickListener deleteListener) {
        this.eventList = events;
        this.starredIds = starredIds != null ? starredIds : new HashSet<>();
        this.starListener = starListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = eventList.get(position);

        holder.artistText.setText(currentEvent.getArtist());
        holder.locationText.setText(currentEvent.getLocation());
        holder.dateText.setText(currentEvent.getDate());

        // Edit event: click on artist name
        holder.artistText.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditEventActivity.class);
            intent.putExtra("event_id", currentEvent.getId());
            v.getContext().startActivity(intent);
        });

        // Star button: filled gold if starred, empty gray if not
        boolean isStarred = starredIds.contains(currentEvent.getId());
        holder.starButton.setText(isStarred ? "★" : "☆");
        holder.starButton.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        isStarred ? Color.parseColor("#FFB300") : Color.parseColor("#888888")
                )
        );
        holder.starButton.setOnClickListener(v -> {
            if (starListener != null) starListener.onStarClick(currentEvent, isStarred);
        });

        // Delete button: hidden if no delete listener (e.g. My Events screen)
        if (deleteListener == null) {
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(currentEvent));
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView artistText, locationText, dateText;
        Button starButton, deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            artistText = itemView.findViewById(R.id.eventArtist);
            locationText = itemView.findViewById(R.id.eventLocation);
            dateText = itemView.findViewById(R.id.eventDate);
            starButton = itemView.findViewById(R.id.starButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public void updateEvents(ArrayList<Event> newEvents) {
        this.eventList = newEvents;
        notifyDataSetChanged();
    }

    public void updateStarredIds(Set<String> newStarredIds) {
        this.starredIds = newStarredIds != null ? newStarredIds : new HashSet<>();
        notifyDataSetChanged();
    }
}
