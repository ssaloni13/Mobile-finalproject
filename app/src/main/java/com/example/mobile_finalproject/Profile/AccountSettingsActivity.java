package com.example.mobile_finalproject.Profile;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_finalproject.Profile.AccountSettings.DeactivateAccountActivity;
import com.example.mobile_finalproject.Profile.AccountSettings.UpdateNameActivity;
import com.example.mobile_finalproject.Profile.AccountSettings.UpdatePasswordActivity;
import com.example.mobile_finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView userProfileImage;
    private TextView updateProfilePictureText, userNameValue, emailValue, passwordValue, closeAccountText;
    // Arrows to redirect to next activity
    private TextView arrow1_name, arrow2_password, arrow3_closeAccount;

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        userProfileImage = findViewById(R.id.user_profile_image);
        updateProfilePictureText = findViewById(R.id.textView_Update_picture);
        updateProfilePictureText.setOnClickListener(this);

        userNameValue = findViewById(R.id.textView_User_Name_Value);
        userNameValue.setOnClickListener(this);
        arrow1_name = findViewById(R.id.arrow1_name);
        arrow1_name.setOnClickListener(this);

        emailValue = findViewById(R.id.textView_Email_Value);

        passwordValue = findViewById(R.id.textView_Password_Value);
        passwordValue.setOnClickListener(this);
        arrow2_password = findViewById(R.id.arrow2_password);
        arrow2_password.setOnClickListener(this);

        closeAccountText = findViewById(R.id.textView_Close_Account_Value);
        closeAccountText.setOnClickListener(this);
        arrow3_closeAccount = findViewById(R.id.arrow3_closeAccount);
        arrow3_closeAccount.setOnClickListener(this);

        displayNameEmail();
    }

    // Helper method to display the name and email of the logged in user
    private void displayNameEmail() {

        String name = user.getDisplayName();

        if (name == null) {
            for (UserInfo userInfo : user.getProviderData()) {
                if (userInfo.getDisplayName() != null) {
                    name = userInfo.getDisplayName();
                }
            }
        }
        userNameValue.setText(name);

        String email = user.getEmail();
        emailValue.setText(email);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // Updating user's profile picture
            case R.id.textView_Update_picture:
                // TODO - Re-implement the camera and gallery feature to update profile image

                break;

            // Updating User Name
            case R.id.arrow1_name:
            case R.id.textView_User_Name_Value:
                startActivity(new Intent(this, UpdateNameActivity.class));
                break;

            // Updating user's password
            case R.id.textView_Password_Value:
            case R.id.arrow2_password:
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;

            // Deactivating user's account
            case R.id.textView_Close_Account_Value:
            case R.id.arrow3_closeAccount:
                // TODO - Code DeactivateAccountActivity
                startActivity(new Intent(this, DeactivateAccountActivity.class));
                break;
        }
    }
}