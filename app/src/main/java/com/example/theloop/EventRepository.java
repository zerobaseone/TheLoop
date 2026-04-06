package com.example.theloop;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class EventRepository {
    // Abstracts the data layer from the UI.

    private static final int PAGE_SIZE = 20;

    private final FirebaseFirestore db;
    private DocumentSnapshot lastVisible = null;

    public interface EventsCallback {
        void onSuccess(ArrayList<Event> events);
        void onError(Exception e);
    }

    public interface EventCallback {
        void onSuccess(Event event);
        void onError(Exception e);
    }

    public interface StarredIdsCallback {
        void onSuccess(Set<String> ids);
        void onError(Exception e);
    }

    public EventRepository() {
        db = FirebaseFirestore.getInstance();
    }

    private String today() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    // Fetches the first page of upcoming events (today or later), sorted by date ascending.
    // Resets pagination state.
    public void getEvents(EventsCallback callback) {
        lastVisible = null;
        db.collection("events")
                .whereGreaterThanOrEqualTo("date", today())
                .orderBy("date")
                .limit(PAGE_SIZE)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        lastVisible = snapshot.getDocuments().get(snapshot.size() - 1);
                    }
                    callback.onSuccess(mapEvents(snapshot.getDocuments()));
                })
                .addOnFailureListener(callback::onError);
    }

    // Fetches the next page of events after the last loaded document.
    public void loadMore(EventsCallback callback) {
        if (lastVisible == null) {
            callback.onSuccess(new ArrayList<>());
            return;
        }
        db.collection("events")
                .whereGreaterThanOrEqualTo("date", today())
                .orderBy("date")
                .startAfter(lastVisible)
                .limit(PAGE_SIZE)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        lastVisible = snapshot.getDocuments().get(snapshot.size() - 1);
                    }
                    callback.onSuccess(mapEvents(snapshot.getDocuments()));
                })
                .addOnFailureListener(callback::onError);
    }

    // Fetches a single event by Firestore document ID.
    public void getEvent(String id, EventCallback callback) {
        db.collection("events").document(id).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        callback.onSuccess(mapEvent(doc));
                    } else {
                        callback.onError(new Exception("Event not found"));
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    public void addEvent(String artist, String location, String date) {
        Map<String, Object> event = new HashMap<>();
        event.put("artist", artist);
        event.put("location", location);
        event.put("date", date);
        event.put("source", "user");
        event.put("createdAt", FieldValue.serverTimestamp());
        db.collection("events").add(event);
    }

    // Saves an event to the current user's savedEvents subcollection
    public void starEvent(Event event) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("artist", event.getArtist());
        data.put("location", event.getLocation());
        data.put("date", event.getDate());
        data.put("genre", event.getGenre());
        db.collection("users").document(uid)
                .collection("savedEvents").document(event.getId())
                .set(data);
    }

    // Removes an event from the current user's savedEvents subcollection
    public void unstarEvent(String eventId) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(uid)
                .collection("savedEvents").document(eventId)
                .delete();
    }

    // Returns the set of event IDs the current user has starred
    public void getStarredEventIds(StarredIdsCallback callback) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(uid).collection("savedEvents").get()
                .addOnSuccessListener(snapshot -> {
                    Set<String> ids = new HashSet<>();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        ids.add(doc.getId());
                    }
                    callback.onSuccess(ids);
                })
                .addOnFailureListener(callback::onError);
    }

    // Returns the full list of starred events for the current user, sorted by date
    public void getStarredEvents(EventsCallback callback) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(uid)
                .collection("savedEvents")
                .orderBy("date")
                .get()
                .addOnSuccessListener(snapshot -> callback.onSuccess(mapEvents(snapshot.getDocuments())))
                .addOnFailureListener(callback::onError);
    }

    public void deleteEvent(String id) {
        db.collection("events").document(id).delete();
    }

    public void updateEvent(String id, String artist, String location, String date) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("artist", artist);
        updates.put("location", location);
        updates.put("date", date);
        db.collection("events").document(id).update(updates);
    }

    private ArrayList<Event> mapEvents(List<DocumentSnapshot> docs) {
        ArrayList<Event> events = new ArrayList<>();
        for (DocumentSnapshot doc : docs) {
            events.add(mapEvent(doc));
        }
        return events;
    }

    private Event mapEvent(DocumentSnapshot doc) {
        return new Event(
                doc.getId(),
                doc.getString("artist"),
                doc.getString("location"),
                doc.getString("date"),
                doc.getString("genre")
        );
    }
}
