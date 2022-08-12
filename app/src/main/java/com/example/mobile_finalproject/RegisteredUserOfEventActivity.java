package com.example.mobile_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;

public class RegisteredUserOfEventActivity extends AppCompatActivity {

    String eventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_user_of_event);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString("eventId");
            System.out.println(eventId + "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }

        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Iterate over all the users(key) in the child users in the db
                for (DataSnapshot userValue : snapshot.getChildren()) {

                    if(userValue.getValue() != null &&
                            userValue.child("eventId").getValue().toString().equals(eventId)) {
                        System.out.println("rao1" + userValue);
                        String name = userValue.child("eventName").getValue().toString();
                        String description = userValue.child("eventDescription").getValue().toString();
                        String eventId = userValue.child("eventId").getValue().toString();

                        System.out.println(name + " " + description + " " + eventId);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}