package com.example.mobile_finalproject.login_registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mobile_finalproject.R;

public class AskingHostOrUserActivity extends AppCompatActivity {

    private Button button_host, button_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asking_host_or_user);

        button_host = findViewById(R.id.button_host);
        button_host.setOnClickListener(v -> openHostActivity());

        button_user = findViewById(R.id.button_user);
        button_user.setOnClickListener(v -> openUserActivity());
    }

    private void openHostActivity() {
        // TODO Open Host activity and send user type: HOST
        Intent intent = new Intent(this, RegisterHostActivity.class);
        startActivity(intent);
    }

    private void openUserActivity() {
        // TODO Open User activity and send user type: USER
        Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
    }
}