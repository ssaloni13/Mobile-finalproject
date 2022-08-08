package com.example.mobile_finalproject.Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobile_finalproject.HostMainActivity;
import com.example.mobile_finalproject.MainActivity;
import android.widget.Button;

import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.Models.SessionManagement;
import android.widget.Button;

import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.Models.SessionManagement;
import com.example.mobile_finalproject.ExampleAdapter;
import com.example.mobile_finalproject.HostMainActivity;
import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.Models.ExampleItem;
import com.example.mobile_finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HostEventsMainActivity extends AppCompatActivity {

    private Button logoutButtonHost;

    ExampleAdapter adapter;
    List<ExampleItem> exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_events_main);

        Intent intent  = new Intent(HostEventsMainActivity.this, HostMainActivity.class);
        //intent.putExtra("userID", intentUsername);

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

        //Intent intent  = new Intent(HostEventsMainActivity.this, HostMainActivity.class);
        //intent.putExtra("userID", intentUsername);
        //startActivity(intent);

    }

}