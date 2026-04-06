package com.example.theloop;

public class Event {
    private String id; // Firestore document ID
    private String artist;
    private String location;
    private String date;
    private String genre;

    public Event(String id, String artist, String location, String date, String genre) {
        this.id = id;
        this.artist = artist;
        this.location = location;
        this.date = date;
        this.genre = genre;
    }

    public String getId() { return id; }
    public String getArtist() { return artist; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getGenre() { return genre; }
}
