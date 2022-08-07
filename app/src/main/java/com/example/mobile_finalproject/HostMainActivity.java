package com.example.mobile_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mobile_finalproject.Models.ExampleItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.List;

public class HostMainActivity extends AppCompatActivity {

    private ExampleAdapter adapter;
    private List<ExampleItem> exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityhostmain);


        fillExampleList();
        setUpRecyclerView();


        ImageView profileview = findViewById(R.id.profile);
        profileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(HostMainActivity.this, HostProfileActivity.class);
                //intent.putExtra("userID", intentUsername);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(HostMainActivity.this, AddEventActivity.class);
                //intent.putExtra("userID", intentUsername);
                startActivity(intent);
            }
        });
    }


    private void fillExampleList() {
        exampleList = new ArrayList<>();

        System.out.println("rao");
        // Connect to the firebase.
        FirebaseDatabase fireBasedatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRefFireBase = fireBasedatabase.getReferenceFromUrl("https://mobile-finalproject-17b4f-default-rtdb.firebaseio.com/");

        // Iterate the child - users
        myRefFireBase.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Iterate over all the users(key) in the child users in the db
                for (DataSnapshot userValue : snapshot.getChildren()) {

                    System.out.println("rao1" + userValue);
                    String name = userValue.child("eventName").getValue().toString();
                    String description = userValue.child("eventDescription").getValue().toString();
                    String address = userValue.child("eventAddress").getValue().toString();

                    System.out.println(name.getClass() + " " + description + " " + address);

                    // Avoid adding the logged in user to the friends list
                    //ArrayList<Integer> a = (ArrayList<Integer>) userValue.child("listOfStickerCounts").getValue();
                    //System.out.println("rao1" + name[0] + " ---- " + uid);
                    exampleList.add(new ExampleItem(R.drawable.ic_launcher_background, name, description));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        
        
        
        exampleList.add(new ExampleItem(R.drawable.ic_launcher_foreground, "One", "Ten"));
        exampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Two", "Eleven"));

        System.out.println("size" + exampleList.size());

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleList);
        System.out.println("ex " + exampleList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

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
    }
}