package com.example.mobile_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mobile_finalproject.Events.HostEventsMainActivity;
import com.example.mobile_finalproject.Models.ExampleItem;
import com.example.mobile_finalproject.Models.SessionManagement;

import java.util.List;

public class HostProfileActivity extends AppCompatActivity {

    private Button logoutButtonHost;

    ExampleAdapter adapter;
    List<ExampleItem> exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_events_main);

        //Intent intent  = new Intent(HostProfileActivity.this, HostMainActivity.class);
        //intent.putExtra("userID", intentUsername);

        logoutButtonHost = findViewById(R.id.button_logout_host);
        logoutButtonHost.setOnClickListener(v -> logout());
    }

    // Helper method to remove the session and log out the user
    private void logout() {
        SessionManagement sessionManagement = new SessionManagement(HostProfileActivity.this);
        sessionManagement.removeSession();

        moveToLoginActivity();
    }

    // Once the session is removed, move the user to login activity
    private void moveToLoginActivity() {
        Intent intent = new Intent(HostProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Intent intent  = new Intent(HostEventsMainActivity.this, HostMainActivity.class);
        //intent.putExtra("userID", intentUsername);
        //startActivity(intent);

    }
}