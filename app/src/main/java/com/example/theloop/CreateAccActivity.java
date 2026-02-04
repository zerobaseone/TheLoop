package com.example.theloop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccActivity extends AppCompatActivity {

    LoginDatabase loginDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);

        loginDb = new LoginDatabase(this);

        EditText usernameEditText = findViewById(R.id.editTextText);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);
        Button createAccountBtn = findViewById(R.id.buttonCreateAccount);

        createAccountBtn.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (loginDb.usernameExists(username)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                loginDb.addLogin(username, password);
                Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();

                // save username in SharedPreferences
                getSharedPreferences("TheLoopPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("loggedInUser", username)
                        .apply();

                // go to events screen
                Intent intent = new Intent(CreateAccActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
