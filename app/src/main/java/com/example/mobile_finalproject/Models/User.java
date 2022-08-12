package com.example.mobile_finalproject.Models;

import java.util.ArrayList;

public class User {

    public String fullName, age, email, typeOfUser;
    public ArrayList<String> registeredevents;

    public User() {

    }

    public User(String fullName, String age, String email, String typeOfUser) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.typeOfUser = typeOfUser;
        this.registeredevents = new ArrayList<>();
    }

    public ArrayList<String> getRegisteredevents(){ return this.registeredevents; }

    public void setRegisteredevents(ArrayList<String> registeredevents) { this.registeredevents = registeredevents; }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
    }
}
