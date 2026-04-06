package com.example.theloop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewEventActivity extends AppCompatActivity {

    private EditText artistEdit, locationEdit, dateEdit;
    private Button saveButton;
    private EventRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newevent);

        artistEdit = findViewById(R.id.editTextArtist);
        locationEdit = findViewById(R.id.editTextLocation);
        dateEdit = findViewById(R.id.editTextDate);
        saveButton = findViewById(R.id.buttonAddEvent);

        repository = new EventRepository();

        // read input fields, validate, save to repository, and return to event list
        saveButton.setOnClickListener(v -> {
            String artist = artistEdit.getText().toString().trim();
            String location = locationEdit.getText().toString().trim();
            String date = dateEdit.getText().toString().trim();

            // ensures no fields are left blank before saving
            if (artist.isEmpty() || location.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // adds event, closes and refreshes the list
            repository.addEvent(artist, location, date);
            Toast.makeText(this, "Event added!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
