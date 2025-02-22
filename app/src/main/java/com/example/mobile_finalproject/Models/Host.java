package com.example.mobile_finalproject.Models;

/**
 * Model Class for Host users.
 */
public class Host {

    public String fullName, age, email, typeOfUser;

    public Host() {
    }

    public Host(String fullName, String age, String email, String typeOfUser) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.typeOfUser = typeOfUser;
    }

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
