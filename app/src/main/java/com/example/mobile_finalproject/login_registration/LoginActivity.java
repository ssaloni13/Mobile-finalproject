package com.example.mobile_finalproject.login_registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobile_finalproject.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password;
    private Button login;
    private TextView forgot_password, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);

        password = findViewById(R.id.password);

        login = findViewById(R.id.login_button);
        login.setOnClickListener(this);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_button:

                break;

            case R.id.forgot_password:

                break;

            case R.id.signup:
                startActivity(new Intent(this, AskingHostOrUserActivity.class));
                break;
        }
    }
}