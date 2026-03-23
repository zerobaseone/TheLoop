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
    private long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);

        artistEdit = findViewById(R.id.editTextArtist);
        locationEdit = findViewById(R.id.editTextLocation);
        dateEdit = findViewById(R.id.editTextDate);
        saveButton = findViewById(R.id.buttonSaveChanges);

        repository = new EventRepository(this);

        // retrieve ID of the event to edit, passed from EventAdapter via Intent
        eventId = getIntent().getLongExtra("event_id", -1);

        // fetch the event from the repository and prefill the fields for editing
        if (eventId != -1) {
            Event event = repository.getEvent(eventId);
            if (event != null) {
                artistEdit.setText(event.getArtist());
                locationEdit.setText(event.getLocation());
                dateEdit.setText(event.getDate());
            }
        }

        saveButton.setText("Save Changes");
        // save the updated values to the repository and return to the event list
        saveButton.setOnClickListener(v -> {
            String artist = artistEdit.getText().toString().trim();
            String location = locationEdit.getText().toString().trim();
            String date = dateEdit.getText().toString().trim();

            repository.updateEvent(eventId, artist, location, date);
            Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
