package com.example.theloop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewEventActivity extends AppCompatActivity {

    private EditText artistEdit, locationEdit, dateEdit;
    private Button saveButton;
    private EventDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newevent);

        // Find the input fields
        artistEdit = findViewById(R.id.editTextArtist);
        locationEdit = findViewById(R.id.editTextLocation);
        dateEdit = findViewById(R.id.editTextDate);

        // Find the button
        saveButton = findViewById(R.id.buttonAddEvent);

        // Initialize database helper
        db = new EventDatabase(this);

        // Button click: save to DB and return
        saveButton.setOnClickListener(v -> {
            String artist = artistEdit.getText().toString().trim();
            String location = locationEdit.getText().toString().trim();
            String date = dateEdit.getText().toString().trim();

            // basic input validation
            // TODO: more validation to prevent sql injection
            if (artist.isEmpty() || location.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add the event to SQLite
            db.addEvent(artist, location, date);

            Toast.makeText(this, "Event added!", Toast.LENGTH_SHORT).show();

            // return to SecondActivity
            finish();
        });
    }
}
