package com.example.mobile_finalproject.Profile.AccountSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobile_finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateNameActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button updateNameButton;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.textView_User_Name);

        updateNameButton = findViewById(R.id.update_name_button);
        updateNameButton.setOnClickListener(v -> updateName());

        progressBar = findViewById(R.id.progress_bar);
    }

    // Helper method to update user's name
    private void updateName() {

        String name = editTextName.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Name can't be blank");
            editTextName.requestFocus();
            return;
        }

        if (name.length() > 20) {
            editTextName.setError("Enter Name under 20 characters");
            editTextName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();

        user.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Name is updated
                Toast.makeText(UpdateNameActivity.this,
                        "Your name has been Updated!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                this.finish();
            } else {
                Toast.makeText(UpdateNameActivity.this,
                        "Error! Unable to change Name", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}