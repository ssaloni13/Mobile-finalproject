package com.example.mobile_finalproject.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Event {

    private String eventName, eventAddress, eventDescription, hostEmailId, eventId;
    private int minAgelimit, maxAgelimit;
    private int eventTicketCost, eventUsersMaxCapacity;
    private String eventStartDate, eventEndDate;
    // TODO - How to store EventStartTime
    private ArrayList<String> registeredusers;

    public Event() {
    }

    public Event(String hostEmailId, String eventName, String eventAddress, String eventDescription, String eventStartDate, String eventEndDate,
    int evenTicketCost, int eventUsersMaxCapacity, int minAgelimit, int maxAgelimit) {
        this.eventId = UUID.randomUUID().toString();
        this.hostEmailId = hostEmailId;
        this.eventName = eventName;
        this.eventAddress = eventAddress;
        this.eventDescription = eventDescription;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventTicketCost = evenTicketCost;
        this.eventUsersMaxCapacity = eventUsersMaxCapacity;
        this.minAgelimit = minAgelimit;
        this.maxAgelimit = maxAgelimit;
        this.registeredusers = new ArrayList<>();
    }

    public String getEventId(){ return  eventId;}

    public void setEventId(String eventId){ this.eventId = eventId; }

    public String getHostEmailId(){ return  hostEmailId;}

    public void setHostEmailId(String hostEmailId){ this.hostEmailId = hostEmailId; }

    public String getEventName() { return eventName; }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) { this.eventAddress = eventAddress; }

    public String getEventDescription() { return eventDescription; }

    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }

    public int getEventTicketCost() { return this.eventTicketCost; }

    public void setEventTicketCost(int eventTicketCost) { this.eventTicketCost = eventTicketCost; }

    public int getEventUsersMaxCapacity() { return this.eventUsersMaxCapacity; }

    public void setEventUsersMaxCapacity(int eventUsersMaxCapacity) { this.eventUsersMaxCapacity = eventUsersMaxCapacity; }

    public int getMinAgelimit() { return this.minAgelimit; }

    public void setMinAgelimit(int minAgelimit) { this.minAgelimit = minAgelimit; }

    public int getMaxAgelimit() { return this.maxAgelimit; }

    public void setMaxAgelimit(int maxAgelimit) { this.maxAgelimit = maxAgelimit; }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) { this.eventStartDate = eventStartDate; }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public ArrayList<String> getRegisteredusers() { return  this.registeredusers;}

    public void  setRegisteredusers(ArrayList<String> h) { this.registeredusers = registeredusers;}
}