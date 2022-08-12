package com.example.mobile_finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_finalproject.Events.EventsListSelectItem1;
import com.example.mobile_finalproject.Models.ExampleItem;
import com.example.mobile_finalproject.Models.ExampleItem1;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//implements Filterable
public class ExampleAdapter1 extends RecyclerView.Adapter<ExampleAdapter1.ExampleViewHolder> {
    private List<ExampleItem1> exampleList;
    private List<ExampleItem1> exampleListFull;
    private EventsListSelectItem1 eventsListSelectItem;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEventName;
        TextView textViewEventEmail;
        CardView eventsListLayout;

        ExampleViewHolder(View itemView) {
            super(itemView);
            textViewEventName = itemView.findViewById(R.id.text_view_event_name1);
            textViewEventEmail = itemView.findViewById(R.id.textView3);
            eventsListLayout = itemView.findViewById((R.id.EventsListCardLayoutMain1));
        }
    }

    public ExampleAdapter1(List<ExampleItem1> exampleList, EventsListSelectItem1 eventsListSelectItem) {
        this.exampleList = exampleList;
        this.eventsListSelectItem = eventsListSelectItem;
        exampleListFull = new ArrayList<>(exampleList);
        System.out.println("ex " + exampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item1,
                parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem1 currentItem = exampleList.get(position);

        //holder.imageView.setImageResource(currentItem.getImageResource());
        //holder.imageView.setImageBitmap(currentItem.getBitmap());


        holder.textViewEventName.setText(currentItem.getName());
        holder.textViewEventEmail.setText(currentItem.getUseremail());

        holder.eventsListLayout.setOnClickListener(v -> eventsListSelectItem.onSelectEventToFullView(exampleList.get(position)));
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }
}