package com.example.mobile_finalproject.Events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mobile_finalproject.R;

public class CurrentRegistrationsActivity extends AppCompatActivity {

    private RecyclerView events;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_registrations);
    }
}