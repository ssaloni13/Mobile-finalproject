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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_finalproject.Models.ExampleItem;

import androidx.cardview.widget.CardView;

import com.example.mobile_finalproject.Events.EventsListSelectItem;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//implements Filterable
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private List<ExampleItem> exampleList;
    private List<ExampleItem> exampleListFull;
    private EventsListSelectItem eventsListSelectItem;
    private StorageReference mStorageStickerReference1;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewEventName;
        TextView textViewEventDescription;
        CardView eventsListLayout;

        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_event_small_img);
            textViewEventName = itemView.findViewById(R.id.text_view_event_name);
            textViewEventDescription = itemView.findViewById(R.id.text_view_event_desc);
            eventsListLayout = itemView.findViewById((R.id.EventsListCardLayoutMain));
        }
    }

    ExampleAdapter(List<ExampleItem> exampleList, EventsListSelectItem eventsListSelectItem) {
        this.exampleList = exampleList;
        this.eventsListSelectItem = eventsListSelectItem;
        exampleListFull = new ArrayList<>(exampleList);
        System.out.println("ex " + exampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,
                parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = exampleList.get(position);

        //holder.imageView.setImageResource(currentItem.getImageResource());
        //holder.imageView.setImageBitmap(currentItem.getBitmap());


        mStorageStickerReference1 = FirebaseStorage.getInstance().getReference().child("Images/" + currentItem.getEventId());
        if(mStorageStickerReference1==null){
            holder.imageView.setImageResource(currentItem.getImageResource());
        }
        else {
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
                        holder.imageView.setImageBitmap(bitmap1);
                    });
        }

        holder.textViewEventName.setText(currentItem.getName());
        holder.textViewEventDescription.setText(currentItem.getDescription());

        holder.eventsListLayout.setOnClickListener(v -> eventsListSelectItem.onSelectEventToFullView(exampleList.get(position)));
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    /*
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }*/

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExampleItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ExampleItem item : exampleListFull) {
                    if (item.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}