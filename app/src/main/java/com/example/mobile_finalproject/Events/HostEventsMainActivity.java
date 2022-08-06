package com.example.mobile_finalproject.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobile_finalproject.HostMainActivity;
import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.R;

public class HostEventsMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_events_main);


        Intent intent  = new Intent(HostEventsMainActivity.this, HostMainActivity.class);
        //intent.putExtra("userID", intentUsername);
        startActivity(intent);
    }
}