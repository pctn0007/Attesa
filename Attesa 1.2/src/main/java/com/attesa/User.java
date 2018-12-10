package com.attesa;
//Project Attesa
// Anthony Paccito [pctn0007] (Team Leader)
// Dariusz Kulpinski [n01164025]
// Winson Vuong [n01104944]

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String name;
    public String email;
    public String phone;
    public String HC;
    public int ustatus;
    public String bio;

    public User(){

    }

    public User(String name, String email, String phone, String HC, int ustatus, String bio) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.HC = HC;
        this.ustatus = ustatus;
        this.bio = bio;

    }



}