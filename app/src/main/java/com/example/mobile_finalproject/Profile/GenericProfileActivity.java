package com.example.mobile_finalproject.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobile_finalproject.ExampleAdapter;
import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.Models.ExampleItem;
import com.example.mobile_finalproject.Models.SessionManagement;
import com.example.mobile_finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class GenericProfileActivity extends AppCompatActivity {

    private TextView textViewUserName;
    private ImageView userProfileImage;
    private Button buttonAccountSettings, buttonManageEvents, buttonLogout;
    private FirebaseUser user;

    ExampleAdapter adapter;
    List<ExampleItem> exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();

        textViewUserName = findViewById(R.id.textView_User_Name);
        textViewUserName.setText(user.getDisplayName());

        userProfileImage = findViewById(R.id.user_profile_image);

        //Intent intent  = new Intent(HostProfileActivity.this, HostMainActivity.class);
        //intent.putExtra("userID", intentUsername);

        buttonAccountSettings = findViewById(R.id.button_account_settings);
        buttonAccountSettings.setOnClickListener(v -> openAccountSettingsActivity());

        buttonManageEvents = findViewById(R.id.button_manage_events);
        buttonManageEvents.setOnClickListener(v -> openManageEventsActivity());

        buttonLogout = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(v -> logout());
    }

    @Override
    protected void onResume() {
        super.onResume();

        textViewUserName.setText(user.getDisplayName());
    }

    // Helper method to open account setting activity
    private void openAccountSettingsActivity() {
        Intent intent = new Intent(GenericProfileActivity.this, AccountSettingsActivity.class);
        startActivity(intent);
    }

    // Helper method to open manage events activity
    private void openManageEventsActivity() {
        Intent intent = new Intent(GenericProfileActivity.this, ManageEventsHostActivity.class);
        startActivity(intent);
    }

    // Helper method to remove the session and log out the user
    private void logout() {
        SessionManagement sessionManagement = new SessionManagement(GenericProfileActivity.this);
        sessionManagement.removeSession();

        moveToLoginActivity();
    }

    // Once the session is removed, move the user to login activity
    private void moveToLoginActivity() {
        Intent intent = new Intent(GenericProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}