package com.example.mobile_finalproject.Models;

import java.util.Date;
import java.util.UUID;

public class Event {

    private String eventName, eventAddress, eventDescription, hostEmailId, eventId;
    private int minAgelimit, maxAgelimit;
    private int eventTicketCost, eventUsersMaxCapacity;
    private Date eventStartDate, eventEndDate;
    // TODO - How to store EventStartTime

    public Event() {
    }

    public Event(String hostEmailId, String eventName, String eventAddress, String eventDescription, Date eventStartDate, Date eventEndDate,
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

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) { this.eventStartDate = eventStartDate; }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }


}