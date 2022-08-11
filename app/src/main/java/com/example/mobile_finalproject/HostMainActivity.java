package com.example.mobile_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mobile_finalproject.Models.ExampleItem;

import com.example.mobile_finalproject.Events.EventsListSelectItem;
import com.example.mobile_finalproject.Events.HostEventsMainActivity;
import com.example.mobile_finalproject.Models.Event;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

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
            System.out.println(hostemail + "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            exampleList = new ArrayList<>();
            this.fillExampleList();
        }

        FloatingActionButton fab = findViewById(R.id.profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(HostMainActivity.this, HostProfileActivity.class);
                intent.putExtra("hostemail", hostemail);
                //intent.putExtra("userID", intentUsername);
                startActivity(intent);
            }
        });

        FloatingActionButton fab1 = findViewById(R.id.add_event);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(hostemail + "111111111111111111111111111111");
                Intent intent  = new Intent(HostMainActivity.this, AddEventActivity.class);
                intent.putExtra("hostemail", hostemail);
                startActivity(intent);
            }
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
                        System.out.println("rao1" + userValue);
                        String name = userValue.child("eventName").getValue().toString();
                        String description = userValue.child("eventDescription").getValue().toString();
                        String eventId = userValue.child("eventId").getValue().toString();

                        System.out.println(name + " " + description + " " + eventId);

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
                                    //v.setImageBitmap(bitmap1);
                                });


                        exampleList.add(new ExampleItem(
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

        //exampleList.add(new ExampleItem(R.drawable.ic_launcher_foreground, "One", "Ten", "rao"));
        //exampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Two", "Eleven", "rao1"));

        System.out.println("size" + exampleList.size());

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleList, HostMainActivity.this);
        System.out.println("ex " + exampleList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelectEventToFullView(ExampleItem currentItem) {


        System.out.println("--------------------" + currentItem.getEventId());
        Intent intent  = new Intent(HostMainActivity.this, EventFullViewActivity.class);
        intent.putExtra("eventId", currentItem.getEventId());
        startActivity(intent);

    }





    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }*/
}