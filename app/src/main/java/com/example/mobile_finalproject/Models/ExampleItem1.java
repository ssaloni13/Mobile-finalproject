package com.example.mobile_finalproject.Models;

import android.graphics.Bitmap;

public class ExampleItem1 {
    private String Name;
    private String Useremail;

    public ExampleItem1(String Name, String Useremail) {
        this.Name = Name;
        this.Useremail = Useremail;
    }

    public String getName(){ return this.Name; }

    public void setName(Bitmap bitmap){ this.Name = Name;}

    public String getUseremail() {
        return this.Useremail;
    }

    public void setUseremail(String Useremail){ this.Useremail = Useremail; }
}