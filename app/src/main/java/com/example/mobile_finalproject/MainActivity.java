package com.example.mobile_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mobile_finalproject.login_registration.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(this, AskingHostOrUserActivity.class));
        /*Intent intent  = new Intent(MainActivity.this, HostMainActivity.class);
        //intent.putExtra("userID", intentUsername);
        startActivity(intent);
        //startActivity(new Intent(this, LoginActivity.class));*/
        //this.finish();

        //This code snippet creates a splash screen effect for 2 seconds
        new Handler().postDelayed(() -> {
            final Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(mainIntent);
            MainActivity.this.finish();
        }, 2000);
    }
}