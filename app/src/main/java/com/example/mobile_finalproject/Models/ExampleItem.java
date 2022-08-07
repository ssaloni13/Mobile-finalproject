package com.example.mobile_finalproject.Models;

public class ExampleItem {
    private int imageResource;
    private String Name;
    private String Description;

    public ExampleItem(int imageResource, String Name, String Description) {
        this.imageResource = imageResource;
        this.Name = Name;
        this.Description = Description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }
}