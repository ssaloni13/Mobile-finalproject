package com.example.mobile_finalproject.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.Models.SessionManagement;
import com.example.mobile_finalproject.R;

public class HostEventsMainActivity extends AppCompatActivity {

    private Button logoutButtonHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_events_main);

        logoutButtonHost = findViewById(R.id.button_logout_host);
        logoutButtonHost.setOnClickListener(v -> logout());
    }

    // Helper method to remove the session and log out the user
    private void logout() {
        SessionManagement sessionManagement = new SessionManagement(HostEventsMainActivity.this);
        sessionManagement.removeSession();

        moveToLoginActivity();
    }

    // Once the session is removed, move the user to login activity
    private void moveToLoginActivity() {
        Intent intent = new Intent(HostEventsMainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}