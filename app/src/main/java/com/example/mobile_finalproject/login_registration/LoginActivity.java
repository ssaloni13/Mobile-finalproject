package com.example.mobile_finalproject.login_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_finalproject.Events.UserEventsListActivity;
import com.example.mobile_finalproject.Events.UserIndividualEventActivity;
import com.example.mobile_finalproject.Models.SessionManagement;
import com.example.mobile_finalproject.HostMainActivity;
import com.example.mobile_finalproject.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private Button login;
    private ImageView logoImage;
    private TextView forgot_password, signup;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private SessionManagement sessionManagement;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = FirebaseAuth.getInstance().getCurrentUser();
        sessionManagement = new SessionManagement(LoginActivity.this);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        logoImage = findViewById(R.id.logo_nuvent);
        login = findViewById(R.id.login_button);
        login.setOnClickListener(this);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);

        progressBar = findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_button:
                userHostLogin();
                break;

            case R.id.forgot_password:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

            case R.id.signup:
                startActivity(new Intent(this, AskingHostOrUserActivity.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for sessions if user is logged in or not
        checkSession();
    }

    // Helper method to check if user is logged in
    private void checkSession() {
        String userID = sessionManagement.getSession();

        if (!userID.equals("null")) {
            // User is logged in and Move to the corresponding activity
            //userHostLogin();
            try {
                redirectUserHostToActivities();
            } catch (Exception e) {
                System.out.println("Can't login via saved session");
            }
        }
        // If user is not logged in, we'll do nothing
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

                // Checking whether the email of the user / host has been verified or not
                try {
                    if (!(user.isEmailVerified())) {
                        // If email is not verified, send the verification link to the user
                        // Verify the email of the user
                        user.sendEmailVerification();
                        Toast.makeText(this,
                                "Check your Email to Verify your Account",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    System.out.println("Can't verify email id due to: " + e);
                }

                // Redirecting user / hosts to their corresponding activities using thread
                redirectUserHostToActivities();

                progressBar.setVisibility(View.GONE);
                this.finish();
            } else {
                Toast.makeText(LoginActivity.this,
                        "Failed to Login! Please check your Credentials",
                        Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Method to redirect the users / hosts to their respective activities
    private void redirectUserHostToActivities() {
        // Databases References of Users & Hosts
        DatabaseReference dRefUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference dRefHosts = FirebaseDatabase.getInstance().getReference().child("Hosts");

        // Checking the user type of the current user,
        // and redirects him accordingly to the events main page

        Runnable fetchUserDataRunnable = () -> {

            dRefUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    List<String> listOfUsersEmail = new ArrayList<>();
                    List<String> age = new ArrayList<>();

                    for (DataSnapshot snapshot: datasnapshot.getChildren()) {
                        String tempEmail = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                        String age1 = Objects.requireNonNull(snapshot.child("age").getValue()).toString();
                        listOfUsersEmail.add(tempEmail);
                        age.add(age1);
                    }

                    try {
                        // If the current User is a Normal user, redirect to UserEventsMainActivity
                        if (listOfUsersEmail.contains(user.getEmail())) {
                            sessionManagement.saveSession(user);
                            sessionManagement.setUserLoggedIn(1);
                            Intent intent = new Intent(LoginActivity.this, UserEventsListActivity.class);
                            intent.putExtra("useremail", user.getEmail());
                            intent.putExtra("userage", age.get(listOfUsersEmail.indexOf(user.getEmail())));
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
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

                    try {
                        // If the current User is a Host user, redirect to HostEventsMainActivity
                        if (listOfHostsEmail.contains(user.getEmail())) {
                            sessionManagement.saveSession(user);
                            sessionManagement.setUserLoggedIn(1);
                            Intent intent = new Intent(LoginActivity.this, HostMainActivity.class);
                            intent.putExtra("hostemail", user.getEmail());
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Error: ", "loadPost:onCancelled", error.toException());
                }
            });

        };

        Thread fetchUserDataThread = new Thread(fetchUserDataRunnable);
        fetchUserDataThread.start();
    }
}