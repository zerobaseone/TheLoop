# The Loop

An Android app for discovering and tracking live music events in Chicago's live music scene.

Originally developed for CS-360 (Mobile Architecture and Programming) and currently being enhanced as part of the CS-499 Computer Science Capstone.

Enhancements can be seen on the `cs499` branch. The `main` branch reflects the original CS-360 submission.

---

## About

The Loop is an Android app that lets users browse upcoming music events in Chicago, star ones they're interested in, and get reminders before they happen. 

---

## Capstone Enhancements (CS-499)

**Enhancement 3 — Databases**
Migrating the local SQLite database to Firebase Firestore. Events are seeded from the Ticketmaster Discovery API, filtered by music events in Chicago venues. 

**Enhancement 1 — Software Engineering & Design**
Replacing the original plaintext credential storage with Firebase Authentication. Also includes: input validation, push notifications replacing deprecated SMS code, and separation of concerns fixes.

---

## Tech Stack

- Java (Android)
- Firebase Authentication
- Firebase Firestore
- Ticketmaster Discovery API
