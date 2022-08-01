package com.example.mobile_finalproject.login_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobile_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = findViewById(R.id.email);

        resetPasswordButton = findViewById(R.id.reset_password_button);
        resetPasswordButton.setOnClickListener(v -> resetPassword());

        progressBar = findViewById(R.id.progress_bar);

        auth = FirebaseAuth.getInstance();
    }

    // Helper method to Reset the password
    private void resetPassword() {

        String email = editTextEmail.getText().toString().toLowerCase().trim();

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

        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // If the reset email has been sent
                Toast.makeText(ForgotPasswordActivity.this,
                        "Check your Email to Reset the Password", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                // TODO - Create a Snack bar that says "This email does not exist in the system, Register"
                //Toast.makeText(ForgotPasswordActivity.this, "Error! Try Again.", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}