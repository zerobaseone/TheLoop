package com.example.theloop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> eventList;
    private EventDatabase db;

    // NEW constructor
    public EventAdapter(ArrayList<Event> events, EventDatabase db) {
        this.eventList = events;
        this.db = db;
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

        // EDIT EVENT: click on artist name
        holder.artistText.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditEventActivity.class);
            intent.putExtra("event_id", currentEvent.getId());
            v.getContext().startActivity(intent);
        });

        // DELETE EVENT: click delete button
        holder.deleteButton.setOnClickListener(v -> {
            // remove from database
            db.deleteEvent(currentEvent.getId());

            // remove from adapter list and refresh RecyclerView
            eventList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, eventList.size());
        });
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

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

    // call this from the activity when the list changes
    public void updateEvents(ArrayList<Event> newEvents) {
        this.eventList = newEvents;
        notifyDataSetChanged();
    }

}
