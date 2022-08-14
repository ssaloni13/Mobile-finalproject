package com.example.mobile_finalproject.login_registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobile_finalproject.HostMainActivity;
import com.example.mobile_finalproject.Models.Host;
import com.example.mobile_finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterHostActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private Button registerHost;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_host);

        mAuth = FirebaseAuth.getInstance();

        editTextFullName = findViewById(R.id.full_name);
        editTextAge = findViewById(R.id.age);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        registerHost = findViewById(R.id.register_host_button);
        registerHost.setOnClickListener(v -> registerNewHost());

        progressBar = findViewById(R.id.progress_bar);
    }

    // Helper method to register new hosts in database
    private void registerNewHost() {

        String fullName = editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String email = editTextEmail.getText().toString().toLowerCase().trim();
        String password = editTextPassword.getText().toString().trim();
        String typeOfUser = "Host User";

        if (fullName.isEmpty()) {
            editTextFullName.setError("Full Name is Required");
            editTextFullName.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            editTextAge.setError("Age is Required");
            editTextAge.requestFocus();
            return;
        }

        if (Integer.parseInt(age) < 18) {
            editTextAge.setError("Min age to register is 18");
            editTextAge.requestFocus();
            return;
        } else if (Integer.parseInt(age) > 100) {
            editTextAge.setError("Enter Valid Age");
            editTextAge.requestFocus();
            return;
        }

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

        // Validating Northeastern email id
        if (!email.contains("@northeastern.edu")) {
            editTextEmail.setError("Northeastern Email ID is required");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Host has been registered successfully

                        // Creating new host as per the Host Model Class
                        Host host = new Host(fullName, age, email, typeOfUser);

                        FirebaseDatabase.getInstance().getReference("Hosts")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(host).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // If the user has been registered & inserted in DB
                                        Toast.makeText(RegisterHostActivity.this,
                                                "Host has been Registered Successfully!",
                                                Toast.LENGTH_LONG).show();

                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(fullName).build();
                                        // Adding Display name for the host
                                        Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileChangeRequest);

                                        progressBar.setVisibility(View.GONE);


                                        // Redirects the Host to the main events page for hosts
                                        Intent intent = new Intent(RegisterHostActivity.this,
                                                HostMainActivity.class);
                                        intent.putExtra("hostemail", email);
                                        finishAffinity();
                                        startActivity(intent);
                                        this.finish();

                                    } else {
                                        Toast.makeText(RegisterHostActivity.this,
                                                "Failed to Register the Host. Try Again!",
                                                Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        Toast.makeText(RegisterHostActivity.this,
                                "Failed to Register the Host. Try Again!",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}