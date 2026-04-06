package com.example.theloop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditEventActivity extends AppCompatActivity {

    private EditText artistEdit, locationEdit, dateEdit;
    private Button saveButton;
    private EventRepository repository;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);

        artistEdit = findViewById(R.id.editTextArtist);
        locationEdit = findViewById(R.id.editTextLocation);
        dateEdit = findViewById(R.id.editTextDate);
        saveButton = findViewById(R.id.buttonSaveChanges);

        repository = new EventRepository();

        // Retrieve the Firestore document ID passed from EventAdapter
        eventId = getIntent().getStringExtra("event_id");

        // Fetch event from Firestore and pre-fill fields for editing
        if (eventId != null) {
            repository.getEvent(eventId, new EventRepository.EventCallback() {
                @Override
                public void onSuccess(Event event) {
                    artistEdit.setText(event.getArtist());
                    locationEdit.setText(event.getLocation());
                    dateEdit.setText(event.getDate());
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(EditEventActivity.this, "Failed to load event", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        saveButton.setText("Save Changes");
        saveButton.setOnClickListener(v -> {
            String artist = artistEdit.getText().toString().trim();
            String location = locationEdit.getText().toString().trim();
            String date = dateEdit.getText().toString().trim();

            if (artist.isEmpty() || location.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            repository.updateEvent(eventId, artist, location, date);
            Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
