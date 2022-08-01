package com.example.mobile_finalproject.login_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private Button login;
    private TextView forgot_password, signup;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        login = findViewById(R.id.login_button);
        login.setOnClickListener(this);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);

        progressBar = findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_button:
                userHostLogin();
                break;

            case R.id.forgot_password:
                // TODO forgot password link

                break;

            case R.id.signup:
                startActivity(new Intent(this, AskingHostOrUserActivity.class));
                break;
        }
    }

    // Helper Method to login user / host
    private void userHostLogin() {

        String email = editTextEmail.getText().toString().toLowerCase().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid Email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // If user is logged in, redirect user / host to the events main page

                // TODO : Check the user type of the current user, and redirects him accordingly to the events main page

                // TODO -> If the user is a normal user, redirect him to UserEventsMainActivity
                // TODO -> Else, redirect him to HostEventsMainActivity

                Log.i("UID of logged in user: ",
                        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()); // This displays the user id in logcat

                progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(LoginActivity.this,
                        "Failed to Login! Please check your Credentials",
                        Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}