package com.example.mobile_finalproject.Profile.AccountSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobile_finalproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordText, newPasswordText, confirmNewPasswordText;
    private Button updatePasswordButton;
    private ProgressBar progressBar;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        user = FirebaseAuth.getInstance().getCurrentUser();

        currentPasswordText = findViewById(R.id.current_password_text);
        newPasswordText = findViewById(R.id.new_password_text);
        confirmNewPasswordText = findViewById(R.id.confirm_new_password_text);

        updatePasswordButton = findViewById(R.id.update_password_button);
        updatePasswordButton.setOnClickListener(v -> updatePassword());

        progressBar = findViewById(R.id.progress_bar);
    }

    // Method to update the user's password
    private void updatePassword() {

        String currentPassword = currentPasswordText.getText().toString().trim();
        String newPassword = newPasswordText.getText().toString().trim();
        String confirmNewPassword = confirmNewPasswordText.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            currentPasswordText.setError("Password can't be blank");
            currentPasswordText.requestFocus();
            return;
        }
        if (currentPassword.length() < 6) {
            currentPasswordText.setError("Min password length should be 6");
            currentPasswordText.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            newPasswordText.setError("Password can't be blank");
            newPasswordText.requestFocus();
            return;
        }
        if (newPassword.length() < 6) {
            newPasswordText.setError("Min password length should be 6");
            newPasswordText.requestFocus();
            return;
        }

        if (confirmNewPassword.isEmpty()) {
            confirmNewPasswordText.setError("Password can't be blank");
            confirmNewPasswordText.requestFocus();
            return;
        }
        if (confirmNewPassword.length() < 6) {
            confirmNewPasswordText.setError("Min password length should be 6");
            confirmNewPasswordText.requestFocus();
            return;
        }

        if (newPassword.equals(currentPassword)) {
            newPasswordText.setError("New password can't be the current password");
            newPasswordText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if (newPassword.equals(confirmNewPassword)) {
            // Before changing the password, re-authenticating the user
            AuthCredential authCredential = EmailAuthProvider.getCredential(
                    Objects.requireNonNull(user.getEmail()), currentPassword);
            user.reauthenticate(authCredential).addOnSuccessListener(unused -> {
                // User is successfully authenticated
                user.updatePassword(newPassword).addOnSuccessListener(unused1 -> {
                    // Password updated
                    Toast.makeText(UpdatePasswordActivity.this,
                            "Your Password has been updated!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    this.finish();
                })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UpdatePasswordActivity.this,
                                    "Error! Couldn't update the password", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        });
            })
                    .addOnFailureListener(e -> {
                        // Authentication failed
                        Toast.makeText(UpdatePasswordActivity.this,
                                "Error! Retry", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    });
        }
    }
}