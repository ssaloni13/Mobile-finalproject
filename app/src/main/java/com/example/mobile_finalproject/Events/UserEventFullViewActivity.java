package com.example.mobile_finalproject.Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_finalproject.ExampleAdapter;
import com.example.mobile_finalproject.Models.Event;
import com.example.mobile_finalproject.Models.ExampleItem;
import com.example.mobile_finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class UserEventFullViewActivity extends AppCompatActivity {

    String eventId, usermail;
    private StorageReference mStorageStickerReference1;
    private TextView editTextEventName, editTextAddress, editTextDes, editTextMax, editTextMin, editTextStart, editTextEnd, editTextCap, editTextCost;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event_full_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString("eventId");
            usermail = extras.getString("usermail");
            System.out.println(eventId + "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }

        Button button1 = findViewById(R.id.registered_users);

        editTextEventName = findViewById(R.id.event_name1);
        editTextAddress = findViewById(R.id.event_address1);
        editTextDes = findViewById(R.id.event_description1);
        editTextMax = findViewById(R.id.event_max1);
        editTextMin = findViewById(R.id.event_min1);
        editTextStart = findViewById(R.id.event_start1);
        editTextEnd = findViewById(R.id.event_end1);
        editTextCap = findViewById(R.id.event_capacity1);
        editTextCost = findViewById(R.id.event_cost1);
        imageView = findViewById(R.id.imageView_event_poster);


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

                        editTextEventName.setText(userValue.child("eventName").getValue().toString());
                        editTextAddress.setText(userValue.child("eventAddress").getValue().toString());
                        editTextDes.setText(userValue.child("eventDescription").getValue().toString());
                        editTextMax.setText(userValue.child("maxAgelimit").getValue().toString());
                        editTextMin.setText(userValue.child("minAgelimit").getValue().toString());
                        editTextStart.setText(userValue.child("eventStartDate").getValue().toString());
                        editTextEnd.setText(userValue.child("eventEndDate").getValue().toString());
                        editTextCap.setText(userValue.child("eventUsersMaxCapacity").getValue().toString());
                        editTextCost.setText(userValue.child("eventTicketCost").getValue().toString());

                        ArrayList<String> events = (ArrayList<String>) userValue.child("registeredusers").getValue();

                        if(events != null) {
                            System.out.println(events);
                            if (events.contains(usermail)) {
                                button1.setText("UNREGISTER");
                            } else {
                                button1.setText("REGISTER");
                            }
                        }

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
                                    imageView.setImageBitmap(bitmap1);
                                });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //For share
        Button button = findViewById(R.id.buttonUserShare);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "Download NUVent to see more events like this!";
                String sub = "Check this event out!";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });


        //For register
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent  = new Intent(UserEventFullViewActivity.this, RegisteredUserOfEventActivity.class);
                //intent.putExtra("eventId", eventId);
                //startActivity(intent);

                // Connect to the firebase.
                FirebaseDatabase fireBasedatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRefFireBase = fireBasedatabase.getReferenceFromUrl("https://mobile-finalproject-17b4f-default-rtdb.firebaseio.com/");


                 myRefFireBase.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // Iterate over all the users(key) in the child users in the db
                        for (DataSnapshot userValue : snapshot.getChildren()) {

                            if(userValue.getValue() != null && userValue.child("eventId").getValue().toString().equals(eventId)) {
                                System.out.println("rao1" + userValue);
                                String name = userValue.child("eventName").getValue().toString();
                                String description = userValue.child("eventDescription").getValue().toString();
                                String eventId = userValue.child("eventId").getValue().toString();

                                ArrayList<String> ar1 = (ArrayList<String>) userValue.child("registeredusers").getValue();

                                if (ar1!=null && ar1.contains(usermail)) {
                                    ar1.remove(usermail);
                                    button1.setText("REGISTER");
                                    Toast.makeText(UserEventFullViewActivity.this, "Unregistered successfully.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    if(ar1==null){
                                        ar1 = new ArrayList<>();
                                    }
                                    ar1.add(usermail);
                                    button1.setText("UNREGISTER");
                                    Toast.makeText(UserEventFullViewActivity.this, "Registered successfully.",
                                            Toast.LENGTH_SHORT).show();
                                }


                                myRefFireBase.child("Events").child(userValue.getKey()).child("registeredusers").setValue(ar1);

                                System.out.println(name + " " + description + " " + eventId);

                                // Avoid adding the logged in user to the friends list
                                //ArrayList<Integer> a = (ArrayList<Integer>) userValue.child("listOfStickerCounts").getValue();
                                //System.out.println("rao1" + name[0] + " ---- " + uid);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                myRefFireBase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // Iterate over all the users(key) in the child users in the db
                        for (DataSnapshot userValue : snapshot.getChildren()) {

                            if(userValue.getValue() != null && userValue.child("email").getValue().toString().equals(usermail)) {

                                ArrayList<String> a1 = (ArrayList<String>) userValue.child("registeredevents").getValue();


                                    if (a1!=null && a1.contains(eventId)) {
                                        a1.remove(eventId);
                                    } else {
                                        if(a1==null){
                                            a1 = new ArrayList<>();
                                        }
                                        a1.add(eventId);
                                    }

                                    myRefFireBase.child("Users").child(userValue.getKey()).child("registeredevents").setValue(a1);
                                // Avoid adding the logged in user to the friends list
                                //ArrayList<Integer> a = (ArrayList<Integer>) userValue.child("listOfStickerCounts").getValue();
                                //System.out.println("rao1" + name[0] + " ---- " + uid);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

    }
}