package com.example.theloop;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
// necessary for SMS messaging
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.AlertDialog;
import android.widget.Toast;


public class NotificationsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        BottomNavMenu.setupBottomNavigation(this, R.id.nav_notifications);

    }
    private static final int SMS_PERMISSION_CODE = 100;

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            // Permission already granted
            sendTestSms();
        }
    }

    private void sendTestSms() {
        String phoneNumber = "1234567890"; // dummy phone # for testing
        String message = "Thank you for signing up for notifications from The Loop app.";

        try {
            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS sent!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "SMS failed to send.", Toast.LENGTH_SHORT).show();
        }
    }


    // handle  the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendTestSms();
            } else {
                //  denied permission; app should still function without SMS
                new AlertDialog.Builder(this)
                        .setTitle("SMS Permission Denied")
                        .setMessage("You will not receive SMS notifications.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }


}
