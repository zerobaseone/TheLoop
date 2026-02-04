package com.example.theloop;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditEventActivity extends AppCompatActivity {

    private EditText artistEdit, locationEdit, dateEdit;
    private Button saveButton;
    private EventDatabase db;
    private long eventId; // Sqlite primary key of event being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent); // your XML file

        // References to UI
        artistEdit = findViewById(R.id.editTextArtist);
        locationEdit = findViewById(R.id.editTextLocation);
        dateEdit = findViewById(R.id.editTextDate);
        saveButton = findViewById(R.id.buttonSaveChanges);

        db = new EventDatabase(this);

        // Get event ID from Intent
        eventId = getIntent().getLongExtra("event_id", -1);

        if (eventId != -1) {
            loadEventData(eventId);
        }

        saveButton.setText("Save Changes");

        saveButton.setOnClickListener(v -> {
            saveChanges();
        });
    }

    private void loadEventData(long id) {
        Cursor cursor = db.getAllEvents();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long currentId = cursor.getLong(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_ID));
                if (currentId == id) {
                    artistEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_ARTIST)));
                    locationEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_LOCATION)));
                    dateEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventDatabase.EventTable.COL_DATE)));
                    break;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void saveChanges() {
        String newArtist = artistEdit.getText().toString().trim();
        String newLocation = locationEdit.getText().toString().trim();
        String newDate = dateEdit.getText().toString().trim();

        db.updateEvent(eventId, newArtist, newLocation, newDate);

        Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}
