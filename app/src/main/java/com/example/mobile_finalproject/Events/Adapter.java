package com.example.mobile_finalproject.Events;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.mobile_finalproject.MainActivity;
import com.example.mobile_finalproject.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    Adapter(Context context, List<String> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = layoutInflater.inflate(R.layout.custom_view,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Bind the data

        String title = data.get(position);
        holder.eventName.setText(title);

        //Add photo later

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
