package com.example.mobile_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobile_finalproject.Events.HostEventsMainActivity;
import com.example.mobile_finalproject.Models.Event;
import com.example.mobile_finalproject.Models.Host;
import com.example.mobile_finalproject.login_registration.RegisterHostActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddEventActivity extends AppCompatActivity {

    String hostemail;
    private FirebaseAuth mAuth;
    private EditText editTextEventName, editTextAddress, editTextDes, editTextMax, editTextMin, editTextStart, editTextEnd, editTextCap, editTextCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hostemail = extras.getString("hostemail");
            System.out.println(hostemail + "fffffffffffffffffffffffffffffff");
        }

        Button b = findViewById(R.id.registerbutton);
        b.setOnClickListener(v -> {
            registerNewEvent(hostemail);
            // Redirects the Host to the main events page for hosts
            Intent intent = new Intent(AddEventActivity.this, HostMainActivity.class);
            intent.putExtra("hostemail", hostemail);
            startActivity(intent);
        });
    }

    // Helper method to register new hosts in database
    private void registerNewEvent(String hostemail) {

        editTextEventName = findViewById(R.id.event_name1);
        editTextAddress = findViewById(R.id.event_address1);
        editTextDes = findViewById(R.id.event_description1);
        editTextMax = findViewById(R.id.event_max1);
        editTextMin = findViewById(R.id.event_min1);
        editTextStart = findViewById(R.id.event_start1);
        editTextEnd = findViewById(R.id.event_end1);
        editTextCap = findViewById(R.id.event_capacity1);
        editTextCost = findViewById(R.id.event_cost1);


        String event_Name = editTextEventName.getText().toString().trim();
        String event_Address = editTextAddress.getText().toString().trim();
        String event_description = editTextDes.getText().toString().trim();
        int event_max = Integer.parseInt(String.valueOf(editTextMax.getText()));
        int event_min = Integer.parseInt(String.valueOf(editTextMin.getText()));
        String event_start = editTextStart.getText().toString().trim();
        String event_end = editTextEnd.getText().toString().trim();
        int event_cap = Integer.parseInt(String.valueOf(editTextCap.getText()));
        int event_cost = Integer.parseInt(String.valueOf(editTextCost.getText()));


        String typeOfUser = "Host User";


        if (event_Name.isEmpty()) {
            editTextEventName.setError("Event Name is Required");
            editTextEventName.requestFocus();
            return;
        }
        if (event_Address.isEmpty()) {
            editTextAddress.setError("Event Address is Required");
            editTextAddress.requestFocus();
            return;
        }
        if (event_description.isEmpty()) {
            editTextEventName.setError("Event Name is Required");
            editTextEventName.requestFocus();
            return;
        }/*
        if (event_max.isEmpty()) {
            editTextAddress.setError("Event Address is Required");
            editTextAddress.requestFocus();
            return;
        }
        if (event_min.isEmpty()) {
            editTextEventName.setError("Event Name is Required");
            editTextEventName.requestFocus();
            return;
        }
        if (event_cap.isEmpty()) {
            editTextAddress.setError("Event Address is Required");
            editTextAddress.requestFocus();
            return;
        }
        if (event_cost.isEmpty()) {
            editTextEventName.setError("Event Name is Required");
            editTextEventName.requestFocus();
            return;
        }
        if (event_start.isEmpty()) {
            editTextAddress.setError("Event Address is Required");
            editTextAddress.requestFocus();
            return;
        }
        if (event_end.isEmpty()) {
            editTextAddress.setError("Event Address is Required");
            editTextAddress.requestFocus();
            return;
        }*/

        FirebaseDatabase fireBasedatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRefFireBase = fireBasedatabase.getReferenceFromUrl("https://mobile-finalproject-17b4f-default-rtdb.firebaseio.com/");

        Event event = new Event(hostemail, event_Name, event_Address, event_description, null, null, event_cost, event_cap, event_max, event_min);

        myRefFireBase.child("Events").push().setValue(event);

        Toast.makeText(AddEventActivity.this,
                "Event has been Registered Successfully!",
                Toast.LENGTH_LONG).show();
    }
}
