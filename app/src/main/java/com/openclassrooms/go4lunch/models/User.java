package com.openclassrooms.go4lunch.models;

import androidx.annotation.Nullable;

public class User {

    private String uid;
    private String name;
    private String email;
    @Nullable
    String urlPicture;

    public User(String uid, String name, String email, @Nullable String urlPicture){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.urlPicture = urlPicture;
    }

    public User() {
    }

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    // --- SETTERS ---
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }


}
