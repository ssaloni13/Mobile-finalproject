package com.example.mobile_finalproject.Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.Models.SessionManagement;
import com.example.mobile_finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserIndividualEventActivity extends AppCompatActivity {

    public ArrayList<String> registeredEvents;
    String eName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_individual_event_layout);

        TextView eventTitle = findViewById(R.id.textViewEventTitle);
        TextView eventDetail = findViewById(R.id.textViewEventDetail);
        Button reg = findViewById(R.id.buttonRegisterEventUser);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeredEvents.add(eName);
                Intent intent = new Intent(getApplicationContext(), CurrentRegistrationsActivity.class);
                startActivity(intent);
            }
        });



        //Display single event
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    eName = snapshot.child("eventName").getValue().toString();
                    String eDetail = "Description: " + snapshot.child("eventDescription").getValue().toString() + ""
                            + "Venue: " + snapshot.child("eventAddress").getValue().toString() + ""
                            + "Cost: " + snapshot.child("eventTicketCost").getValue().toString() + ""
                            + "Age Limit: " + snapshot.child("minAgelimit").getValue().toString() + " - "
                            + snapshot.child("maxAgelimit").getValue().toString() + ""
                            + "Contact host: " + snapshot.child("hostEmailId").getValue().toString();
                    eventTitle.setText(eName);
                    eventDetail.setText(eDetail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Error: ", "loadPost:onCancelled", error.toException());
            }
        });
    }
}