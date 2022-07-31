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

import com.example.mobile_finalproject.Models.User;
import com.example.mobile_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private Button registerUser;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        registerUser = findViewById(R.id.register_user_button);
        registerUser.setOnClickListener(this);

        editTextFullName = findViewById(R.id.full_name);
        editTextAge = findViewById(R.id.age);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_user_button:
                registerUser();
                break;
        }
    }

    // Helper method to register the user
    private void registerUser() {

        String fullName = editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String email = editTextEmail.getText().toString().toLowerCase().trim();
        String password = editTextPassword.getText().toString().trim();
        String typeOfUser = "Normal User";

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
                        // User has been registered successfully
                        User user = new User(fullName, age, email, typeOfUser);

                                // Gives us the ID of the registered user
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    // To check if data has been inserted into database or not
                                    if (task1.isSuccessful()) {
                                        // If the user has been registered & inserted in DB
                                        Toast.makeText(RegisterUserActivity.this,
                                                "User has been Registered Successfully!",
                                                Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        // TODO Redirect the user to the main events page


                                    } else {
                                        Toast.makeText(RegisterUserActivity.this,
                                                "Failed to Register the User. Try Again!",
                                                Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        Toast.makeText(RegisterUserActivity.this,
                                "Failed to Register the User. Try Again!",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}