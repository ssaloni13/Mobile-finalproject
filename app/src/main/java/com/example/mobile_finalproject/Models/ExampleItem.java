package com.example.mobile_finalproject.Models;

import android.graphics.Bitmap;

public class ExampleItem {
    private int imageResource;
    private String Name;
    private String Description;
    private String eventId;
    private Bitmap bitmap;

    public ExampleItem(Bitmap bitmap, int imageResource, String Name, String Description, String eventId) {
        this.bitmap = bitmap;
        this.imageResource = imageResource;
        this.Name = Name;
        this.Description = Description;
        this.eventId = eventId;
    }

    public Bitmap getBitmap(){ return this.bitmap; }

    public void setBitmap(Bitmap bitmap){ this.bitmap = bitmap;}

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource){ this.imageResource = imageResource; }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId){ this.eventId = eventId; }

    public String getName() {
        return Name;
    }

    public void setName(String Name){ this.Name = Name; }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description){ this.Description = Description; }

}