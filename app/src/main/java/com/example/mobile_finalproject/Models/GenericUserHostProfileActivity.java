package com.example.mobile_finalproject.Models;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mobile_finalproject.R;

/**
 * Generic Profile Activity that can be used to display the Profile settings of
 * both the Hosts and the Normal Users.
 */
public class GenericUserHostProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_user_host_profile);
    }
}