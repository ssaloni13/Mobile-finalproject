package com.example.mobile_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mobile_finalproject.Models.ExampleItem;

import com.example.mobile_finalproject.Events.EventsListSelectItem;

import com.example.mobile_finalproject.Profile.GenericProfileActivity;
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
import java.util.List;

public class HostMainActivity extends AppCompatActivity implements EventsListSelectItem {

    ExampleAdapter adapter;
    List<ExampleItem> exampleList;
    String hostemail;
    FirebaseDatabase fireBasedatabase;
    DatabaseReference myRefFireBase;
    RecyclerView recyclerViewFriendsList;
    private StorageReference mStorageStickerReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityhostmain);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hostemail = extras.getString("hostemail");
            exampleList = new ArrayList<>();
            this.fillExampleList();
        }

        com.getbase.floatingactionbutton.FloatingActionButton fab = findViewById(R.id.profile);
        fab.setOnClickListener(v -> {
            Intent intent  = new Intent(HostMainActivity.this, GenericProfileActivity.class);
            intent.putExtra("hostemail", hostemail);
            startActivity(intent);
        });

        com.getbase.floatingactionbutton.FloatingActionButton fab1 = findViewById(R.id.add_event);
        fab1.setOnClickListener(view -> {
            Intent intent  = new Intent(HostMainActivity.this, AddEventActivity.class);
            intent.putExtra("hostemail", hostemail);
            startActivity(intent);
        });
    }

    private void fillExampleList() {
        exampleList = new ArrayList<>();
        System.out.println("rao");

        // Iterate the child - users
        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Iterate over all the users(key) in the child users in the db
                for (DataSnapshot userValue : snapshot.getChildren()) {

                    if(userValue.getValue() != null &&
                            userValue.child("hostEmailId").getValue().toString().equals(hostemail)) {
                        String name = userValue.child("eventName").getValue().toString();
                        String description = userValue.child("eventDescription").getValue().toString();
                        String eventId = userValue.child("eventId").getValue().toString();

                        // Avoid adding the logged in user to the friends list
                        //ArrayList<Integer> a = (ArrayList<Integer>) userValue.child("listOfStickerCounts").getValue();
                        //System.out.println("rao1" + name[0] + " ---- " + uid);

                        ImageView v = null;
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
                                    System.out.println("000000000" + bitmap1);
                                });


                        Bitmap bitmap1 = null;

                        exampleList.add(new ExampleItem(
                                bitmap1,
                                R.drawable.ic_launcher_background,
                                name, description, eventId));
                    }
                }

                // Set the adapter to the list created
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(HostMainActivity.this));
                recyclerView.setAdapter(new ExampleAdapter(exampleList, HostMainActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleList, HostMainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelectEventToFullView(ExampleItem currentItem) {
        Intent intent  = new Intent(HostMainActivity.this, EventFullViewActivity.class);
        intent.putExtra("eventId", currentItem.getEventId());
        startActivity(intent);
        finish();
    }
}