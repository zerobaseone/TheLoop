package com.example.theloop;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class EventRepository {
    // Module 3 - Separation of concerns
    // This file Abstracts the data layer from the UI
    // To change the data source to Firestore, only this file has to change!
    private final EventDatabase db;

    public EventRepository(Context context) {
        db = new EventDatabase(context);
    }

    public ArrayList<Event> getEvents() {
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

    // CRUD methods
    public Event getEvent(long id) {
        Cursor cursor = db.getAllEvents();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long currentId = cursor.getLong(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_ID));
                if (currentId == id) {
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_ARTIST));
                    String location = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_LOCATION));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_DATE));
                    cursor.close();
                    return new Event(id, artist, location, date);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return null;
    }

    public void addEvent(String artist, String location, String date) {
        db.addEvent(artist, location, date);
    }

    public void deleteEvent(long id) {
        db.deleteEvent(id);
    }

    public void updateEvent(long id, String artist, String location, String date) {
        db.updateEvent(id, artist, location, date);
    }
}
