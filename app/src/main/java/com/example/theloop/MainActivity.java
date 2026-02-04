package com.example.theloop;

import android.content.Intent;
import android.content.SharedPreferences; // to display username
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LoginDatabase loginDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginDb = new LoginDatabase(this);

        EditText usernameEditText = findViewById(R.id.editTextText);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);

        // LOGIN button
        Button loginButton = findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(v -> {
            String usernameInput = ((EditText) findViewById(R.id.editTextText)).getText().toString();
            String passwordInput = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();

            LoginDatabase db = new LoginDatabase(this);
            boolean success = db.checkLogin(usernameInput, passwordInput); // implement this in LoginDatabase

            if (success) {
                // Save username in SharedPreferences
                SharedPreferences prefs = getSharedPreferences("TheLoopPrefs", MODE_PRIVATE);
                prefs.edit().putString("loggedInUser", usernameInput).apply();

                // Go to main event screen
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show();
            }
        });


        // SIGN UP button
        Button signUpButton = findViewById(R.id.signupbutton);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAccActivity.class);
            startActivity(intent);
        });
    }
}
