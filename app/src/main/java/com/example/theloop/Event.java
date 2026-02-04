package com.example.theloop;

public class Event {
    private long id; // SQLite primary key
    private String artist;
    private String location;
    private String date;

    public Event(long id, String artist, String location, String date) {
        this.id = id;
        this.artist = artist;
        this.location = location;
        this.date = date;
    }

    public long getId() { return id; }
    public String getArtist() {
        return artist;
    }
    public String getLocation() {
        return location;
    }
    public String getDate() {
        return date;
    }
}
