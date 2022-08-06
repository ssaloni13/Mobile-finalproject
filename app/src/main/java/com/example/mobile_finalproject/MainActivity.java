package com.example.mobile_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mobile_finalproject.login_registration.AskingHostOrUserActivity;
import android.view.View;
import android.widget.Button;

import com.example.mobile_finalproject.Events.HostEventsMainActivity;
import com.example.mobile_finalproject.login_registration.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This code snippet creates a splash screen effect for 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, 5000);

        //startActivity(new Intent(this, AskingHostOrUserActivity.class));
        /*Intent intent  = new Intent(MainActivity.this, HostMainActivity.class);
        //intent.putExtra("userID", intentUsername);
        startActivity(intent);
        //startActivity(new Intent(this, LoginActivity.class));*/
        //this.finish();
    }
}