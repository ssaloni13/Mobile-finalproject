package com.example.mobile_finalproject.Models;

public class ExampleItem {
    private int imageResource;
    private String Name;
    private String Description;
    private String eventId;

    public ExampleItem(int imageResource, String Name, String Description, String eventId) {
        this.imageResource = imageResource;
        this.Name = Name;
        this.Description = Description;
        this.eventId = eventId;
    }

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