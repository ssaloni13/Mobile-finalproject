package com.example.mobile_finalproject.login_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mobile_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        editTextEmail = findViewById(R.id.email);

        resetPasswordButton = findViewById(R.id.reset_password_button);
        resetPasswordButton.setOnClickListener(v -> resetPassword());

        progressBar = findViewById(R.id.progress_bar);

        this.auth = FirebaseAuth.getInstance();

        //
        constraintLayout = findViewById(R.id.constraint);
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

        this.auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // If the reset email has been sent
                Toast.makeText(ForgotPasswordActivity.this,
                        "Check your Email to Reset the Password", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                Snackbar snackbar = Snackbar.make(constraintLayout, "", Snackbar.LENGTH_INDEFINITE);
                View custom = getLayoutInflater().inflate(R.layout.snackbar_layout, null);
                snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                snackbarLayout.setPadding(0,200,0,0);


                (custom.findViewById(R.id.registerText)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), AskingHostOrUserActivity.class);
                        startActivity(intent);
                    }
                });

                snackbarLayout.addView(custom, 0);
                snackbar.show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}