package com.example.mobile_finalproject.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobile_finalproject.Events.UserEventFullViewActivity;
import com.example.mobile_finalproject.Events.UserEventsListActivity;
import com.example.mobile_finalproject.ExampleAdapter;
import com.example.mobile_finalproject.HostMainActivity;
import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.Models.ExampleItem;
import com.example.mobile_finalproject.Models.SessionManagement;
import com.example.mobile_finalproject.R;
import com.example.mobile_finalproject.login_registration.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenericProfileActivity extends AppCompatActivity {

    private TextView textViewUserName;
    private ImageView userProfileImage;
    private Button buttonAccountSettings, buttonManageEvents, buttonLogout;
    private FirebaseUser user;
    public String useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_profile);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            useremail = extras.getString("useremail");
            //useremail = extras.getString("hostemail");
            System.out.println(useremail + "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }

        user = FirebaseAuth.getInstance().getCurrentUser();

        textViewUserName = findViewById(R.id.textView_User_Name);
        textViewUserName.setText(user.getDisplayName());

        userProfileImage = findViewById(R.id.user_profile_image);

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

    // Method to redirect the users / hosts to their respective manage events activities
    private void openManageEventsActivity() {
        // Databases References of Users & Hosts
        DatabaseReference dRefUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference dRefHosts = FirebaseDatabase.getInstance().getReference().child("Hosts");

        // Checking the user type of the current user,
        // and redirects him accordingly to their manage events page

        dRefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                List<String> listOfUsersEmail = new ArrayList<>();

                for (DataSnapshot snapshot: datasnapshot.getChildren()) {
                    String tempEmail = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    listOfUsersEmail.add(tempEmail);
                }

                /*
                // If the current User is a Normal user, redirect to ManageEventsUserActivity
                if (listOfUsersEmail.contains(user.getEmail())) {
                    Intent intent = new Intent(GenericProfileActivity.this, ManageEventsUserActivity.class);
                    startActivity(intent);
                }*/

                if (listOfUsersEmail.contains(user.getEmail())) {
                    //redirect to user activitys list
                    Intent intent = new Intent(GenericProfileActivity.this, UserEventsListActivity.class);
                    System.out.println("ccccccccccccccccccccccccccc" + useremail + user.getEmail());
                    intent.putExtra("useremail", user.getEmail());
                    intent.putExtra("userage", "-1");
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Error: ", "loadPost:onCancelled", error.toException());
            }
        });

        dRefHosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                List<String> listOfHostsEmail = new ArrayList<>();

                for (DataSnapshot snapshot: datasnapshot.getChildren()) {
                    String tempEmail = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    listOfHostsEmail.add(tempEmail);
                }

                /*
                // If the current User is a Host user, redirect to ManageEventsHostActivity
                if (listOfHostsEmail.contains(user.getEmail())) {
                    Intent intent = new Intent(GenericProfileActivity.this, ManageEventsHostActivity.class);
                    startActivity(intent);
                }*/

                //redirect to host activitys list
                if (listOfHostsEmail.contains(user.getEmail())) {
                    Intent intent = new Intent(GenericProfileActivity.this, HostMainActivity.class);
                    intent.putExtra("useremail", user.getEmail());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Error: ", "loadPost:onCancelled", error.toException());
            }
        });
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