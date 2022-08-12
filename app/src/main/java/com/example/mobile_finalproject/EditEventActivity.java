package com.example.mobile_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_finalproject.Models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class EditEventActivity extends AppCompatActivity {

    String eventId, hostEmailId;
    private StorageReference mStorageStickerReference1;
    private ImageView imageview;
    private EditText editTextEventName, editTextAddress, editTextDes, editTextMax, editTextMin, editTextStart, editTextEnd, editTextCap, editTextCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString("eventId");
            System.out.println(eventId + "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }

        editTextEventName = findViewById(R.id.event_name1);
        editTextAddress = findViewById(R.id.event_address1);
        editTextDes = findViewById(R.id.event_description1);
        editTextMax = findViewById(R.id.event_max1);
        editTextMin = findViewById(R.id.event_min1);
        editTextStart = findViewById(R.id.event_start1);
        editTextEnd = findViewById(R.id.event_end1);
        editTextCap = findViewById(R.id.event_capacity1);
        editTextCost = findViewById(R.id.event_cost1);
        imageview = findViewById(R.id.imageView_event_poster);


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

                        System.out.println(userValue);
                        System.out.println(userValue.child("eventStartDate").getValue().toString());


                        hostEmailId = userValue.child("hostEmailId").getValue().toString();

                        editTextEventName.setText(userValue.child("eventName").getValue().toString());
                        editTextAddress.setText(userValue.child("eventAddress").getValue().toString());
                        editTextDes.setText(userValue.child("eventDescription").getValue().toString());
                        editTextMax.setText(userValue.child("maxAgelimit").getValue().toString());
                        editTextMin.setText(userValue.child("minAgelimit").getValue().toString());
                        editTextStart.setText(userValue.child("eventStartDate").getValue().toString());
                        editTextEnd.setText(userValue.child("eventEndDate").getValue().toString());
                        editTextCap.setText(userValue.child("eventUsersMaxCapacity").getValue().toString());
                        editTextCost.setText(userValue.child("eventTicketCost").getValue().toString());

                        mStorageStickerReference1 = FirebaseStorage.getInstance().getReference().child("Images/" + eventId);
                        if(mStorageStickerReference1==null){ continue;}
                        File localFileSticker1 = null;
                        try {
                            localFileSticker1 = File.createTempFile("sticker1", "jpg");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        File finalLocalFileSticker = localFileSticker1;
                        mStorageStickerReference1.getFile(localFileSticker1)
                                .addOnSuccessListener(taskSnapshot -> {
                                    Bitmap bitmap1 = BitmapFactory.decodeFile(finalLocalFileSticker.getAbsolutePath());
                                    imageview.setImageBitmap(bitmap1);
                                });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button button = findViewById(R.id.submiteditbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editsubmit();
                Intent intent  = new Intent(EditEventActivity.this, HostMainActivity.class);
                intent.putExtra("hostemail", hostEmailId);
                startActivity(intent);
            }
        });


    }



    public void editsubmit(){


        String event_Name = editTextEventName.getText().toString().trim();
        String event_Address = editTextAddress.getText().toString().trim();
        String event_description = editTextDes.getText().toString().trim();
        int event_max = Integer.parseInt(String.valueOf(editTextMax.getText()));
        int event_min = Integer.parseInt(String.valueOf(editTextMin.getText()));
        String event_start = editTextStart.getText().toString().trim();
        String event_end = editTextEnd.getText().toString().trim();
        int event_cap = Integer.parseInt(String.valueOf(editTextCap.getText()));
        int event_cost = Integer.parseInt(String.valueOf(editTextCost.getText()));


        FirebaseDatabase fireBasedatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRefFireBase = fireBasedatabase.getReferenceFromUrl("https://mobile-finalproject-17b4f-default-rtdb.firebaseio.com/");

        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Iterate over all the users(key) in the child users in the db
                for (DataSnapshot userValue : snapshot.getChildren()) {

                    if(userValue.getValue() != null &&
                            userValue.child("eventId").getValue().toString().equals(eventId)) {
                        System.out.println("rao1" + userValue);
                        String hostemail = userValue.child("hostEmailId").getValue().toString();

                        Event event = new Event(hostemail, event_Name, event_Address, event_description, event_start, event_end, event_cost, event_cap, event_min, event_max);
                        event.setEventId(eventId);

                        myRefFireBase.child("Events").child(userValue.getKey()).setValue(event);

                        Toast.makeText(EditEventActivity.this,
                                "Event has been Edited Successfully!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}