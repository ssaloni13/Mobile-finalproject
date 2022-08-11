package com.example.mobile_finalproject.Events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_finalproject.R;

import java.util.ArrayList;

public class newAdapter extends RecyclerView.Adapter {

    private ArrayList<String> names;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView eventRName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventRName = itemView.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.registeredadapter, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Debug and try new code
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
