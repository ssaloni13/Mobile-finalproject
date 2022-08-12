package com.example.mobile_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.widget.TextView;

import com.example.mobile_finalproject.Events.EventsListSelectItem;
import com.example.mobile_finalproject.Events.EventsListSelectItem1;
import com.example.mobile_finalproject.Models.ExampleItem;
import com.example.mobile_finalproject.Models.ExampleItem1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisteredUserOfEventActivity extends AppCompatActivity implements EventsListSelectItem1{

    String eventId;
    List<ExampleItem1> exampleList;
    TextView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_user_of_event);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString("eventId");
            System.out.println(eventId + "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }

        v = findViewById(R.id.textView2);
        filllist();
    }


    public void filllist(){
        exampleList = new ArrayList<>();

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

                        ArrayList<String> ar1 = (ArrayList<String>) userValue.child("registeredusers").getValue();
                        System.out.println(ar1);
                        if(ar1 != null){
                            v.setText("Registered Users Count :" + ar1.size());
                            for(int i=0; i<ar1.size(); i++){
                                exampleList.add(new ExampleItem1(ar1.get(i).split("@")[0], ar1.get(i)));
                            }
                        }
                    }
                }
                System.out.println(exampleList + "chary");
                // Set the adapter to the list created
                RecyclerView recyclerView = findViewById(R.id.recyclerView2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(RegisteredUserOfEventActivity.this));
                recyclerView.setAdapter(new ExampleAdapter1(exampleList, RegisteredUserOfEventActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onSelectEventToFullView(ExampleItem1 currentItem){

    }

}