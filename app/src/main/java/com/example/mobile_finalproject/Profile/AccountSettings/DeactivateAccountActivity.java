package com.example.mobile_finalproject.Profile.AccountSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class DeactivateAccountActivity extends AppCompatActivity {

    private EditText editTextDeactivate;
    private Button deactivateButton;
    private ProgressBar progressBar;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_account);

        user = FirebaseAuth.getInstance().getCurrentUser();

        editTextDeactivate = findViewById(R.id.editText_deactivate);

        deactivateButton = findViewById(R.id.deactivate_button);
        deactivateButton.setOnClickListener(v -> deactivateAccount());

        progressBar = findViewById(R.id.progress_bar);
    }

    private void deactivateAccount() {
        String deactivateText = editTextDeactivate.getText().toString().trim();

        if (!deactivateText.equals("DELETE")) {
            editTextDeactivate.setError("Enter DELETE");
            editTextDeactivate.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Before changing the password, re-authenticating the user

        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(DeactivateAccountActivity.this,
                        "Account Successfully Deleted!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                progressBar.setVisibility(View.GONE);
                this.finish();
            } else {
                Toast.makeText(this, "Error! Retry", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}