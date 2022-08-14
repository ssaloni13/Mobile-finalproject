package com.example.mobile_finalproject.Profile.AccountSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.Models.SessionManagement;
import com.example.mobile_finalproject.Profile.GenericProfileActivity;
import com.example.mobile_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeactivateAccountActivity extends AppCompatActivity {

    private EditText editTextPassword;
    private Button deactivateButton;
    private ProgressBar progressBar;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_account);

        user = FirebaseAuth.getInstance().getCurrentUser();

        editTextPassword = findViewById(R.id.editText_Password);

        deactivateButton = findViewById(R.id.deactivate_button);
        deactivateButton.setOnClickListener(v -> deactivateAccount());

        progressBar = findViewById(R.id.progress_bar);
    }

    private void deactivateAccount() {
        String passwordText = editTextPassword.getText().toString().trim();

        if (passwordText.isEmpty()) {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        // Before changing the password, re-authenticating the user

        AlertDialog.Builder dialog = new AlertDialog.Builder(DeactivateAccountActivity.this);
        dialog.setTitle("Are you sure you want to Delete your Account?");
        dialog.setMessage("Deleting this account will result in completely removing your account " +
                "from the system and you won't be able to access the app.");

        dialog.setPositiveButton("DELETE", (dialog12, which) -> {
            progressBar.setVisibility(View.VISIBLE);
            reAuthenticateUser();
        });

        dialog.setNegativeButton("Dismiss", (dialog1, which) -> dialog1.dismiss());

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    // Helper method to Re-Authenticate the user
    private void reAuthenticateUser() {
        // Re-authenticate user
        AuthCredential credential = EmailAuthProvider.getCredential(
                user.getEmail(), editTextPassword.getText().toString().trim());

        user.reauthenticate(credential).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                // User is authenticated
                deleteUser();
            } else {
                Toast.makeText(DeactivateAccountActivity.this,
                        "Incorrect Password!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
        });
    }

    // Helper method to delete the user
    private void deleteUser() {
        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // If the account is successfully deleted
                Toast.makeText(DeactivateAccountActivity.this,
                        "Account Successfully Deleted!", Toast.LENGTH_LONG).show();

                logout();

                progressBar.setVisibility(View.GONE);
                DeactivateAccountActivity.this.finish();
            } else {
                Toast.makeText(DeactivateAccountActivity.this,
                        "Error! Retry", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Helper method to remove the session and log out the user
    private void logout() {
        SessionManagement sessionManagement = new SessionManagement(DeactivateAccountActivity.this);
        sessionManagement.removeSession();

        moveToLoginActivity();
    }

    // Once the session is removed, move the user to login activity
    private void moveToLoginActivity() {
        Intent intent = new Intent(DeactivateAccountActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }
}