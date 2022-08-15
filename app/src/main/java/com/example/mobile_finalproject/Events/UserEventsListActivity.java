package com.example.mobile_finalproject.Events;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.mobile_finalproject.ExampleAdapter;
import com.example.mobile_finalproject.Profile.GenericProfileActivity;
import com.example.mobile_finalproject.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.mobile_finalproject.Models.ExampleItem;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class UserEventsListActivity extends AppCompatActivity implements EventsListSelectItem {

    String[] months = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    int currentDay;
    int currentMonth;
    int currentYear;
    ExampleAdapter adapter;
    List<ExampleItem> exampleList;
    String useremail;
    int userage;
    FirebaseDatabase fireBasedatabase;
    DatabaseReference myRefFireBase;
    RecyclerView recyclerViewFriendsList;
    private StorageReference mStorageStickerReference1;
    private ArrayList<String> registeredevents;
    private int next = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events_list);

        Calendar cal = Calendar.getInstance();
        currentDay = cal.get(Calendar.DATE);
        currentMonth = cal.get(Calendar.MONTH) + 1;
        currentYear = cal.get(Calendar.YEAR);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            System.out.println(extras);

            useremail = extras.getString("useremail");
            try {
                userage = Integer.parseInt(extras.getString("userage"));
            } catch (Exception e) {
                System.out.println(e);
            }
            exampleList = new ArrayList<>();
            this.fillExampleList();
        }

        // FAB 1 - Settings
        FloatingActionButton fab2 = findViewById(R.id.profile);
        fab2.setOnClickListener(v -> {
            Intent intent  = new Intent(UserEventsListActivity.this, GenericProfileActivity.class);
            intent.putExtra("useremail", useremail);
            startActivity(intent);
        });
    }


    private void fillExampleList() {
        exampleList = new ArrayList<>();

        Runnable fillExampleListRunnable = () -> {

            // Iterate the child - users
            FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // Iterate over all the users(key) in the child users in the db
                    for (DataSnapshot userValue : snapshot.getChildren()) {

                        if(userValue.getValue() != null) {

                            String[] s = userValue.child("eventEndDate").getValue().toString().split("/");

                            if (
                                    userage >= Integer.parseInt(userValue.child("minAgelimit").getValue().toString()) &&
                                            userage <= Integer.parseInt(userValue.child("maxAgelimit").getValue().toString()) &&
                                            currentYear <= Integer.parseInt(s[2]) &&
                                            currentMonth <= Integer.parseInt(s[1]) &&
                                            currentDay <= Integer.parseInt(s[0])

                            ){
                                String name = userValue.child("eventName").getValue().toString();
                                String description = userValue.child("eventDescription").getValue().toString();
                                String eventId = userValue.child("eventId").getValue().toString();

                                System.out.println(name + " " + description + " " + eventId);

                                // Avoid adding the logged in user to the friends list
                                //ArrayList<Integer> a = (ArrayList<Integer>) userValue.child("listOfStickerCounts").getValue();

                                ImageView v = null;
                                mStorageStickerReference1 = FirebaseStorage.getInstance().getReference().child("Images/" + eventId);
                                if (mStorageStickerReference1 == null) {
                                    continue;
                                }
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
                                            //v.setImageBitmap(bitmap1);
                                            System.out.println("000000000" + bitmap1);
                                        });


                                Bitmap bitmap1 = null;

                                exampleList.add(new ExampleItem(
                                        bitmap1,
                                        R.drawable.ic_launcher_background,
                                        name, description, eventId));

                            }
                        }
                    }

                    // Set the adapter to the list created
                    RecyclerView recyclerView = findViewById(R.id.recycler_view);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(UserEventsListActivity.this));
                    recyclerView.setAdapter(new ExampleAdapter(exampleList, UserEventsListActivity.this));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        };

        Thread fillExampleListThread = new Thread(fillExampleListRunnable);
        fillExampleListThread.start();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleList, UserEventsListActivity.this);
        System.out.println("ex " + exampleList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelectEventToFullView(ExampleItem currentItem) {
        Intent intent  = new Intent(UserEventsListActivity.this, UserEventFullViewActivity.class);
        intent.putExtra("usermail", useremail);
        intent.putExtra("eventId", currentItem.getEventId());
        intent.putExtra("userage", Integer.toString(userage));
        startActivity(intent);
        UserEventsListActivity.this.finish();
    }
}