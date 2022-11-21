package com.openclassrooms.go4lunch.models;

import android.telecom.Call;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class User {

    private String uid;
    private String name;
    private String email;
    private String nextLunchRestaurantId;
    private String nextLunchRestaurantName;
    @Nullable
    String urlPicture;
    private ArrayList<LikedRestaurant> likedRestaurants;
    private String fcmToken;
    private Boolean isNotified;

    public User(String uid, String name, String email, @Nullable String urlPicture,
                String nextLunchRestaurantId, String nextLunchRestaurantName, ArrayList<LikedRestaurant> likedRestaurants, String fcmToken, Boolean isNotified){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.urlPicture = urlPicture;
        this.nextLunchRestaurantId = null;
        this.nextLunchRestaurantName = null;
        this.likedRestaurants = null;
        this.fcmToken = fcmToken;
        this.isNotified = isNotified;
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
    public String getNextLunchRestaurantId() { return nextLunchRestaurantId;}
    public String getNextLunchRestaurantName() { return nextLunchRestaurantName;}
    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }
    public ArrayList<LikedRestaurant> getLikedRestaurants() {return likedRestaurants;}
    public String getFcmToken() { return fcmToken; }
    public Boolean getIsNotified() { return isNotified; }

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
    public void setNextLunchRestaurantId(String nextLunchRestaurantId) {
        this.nextLunchRestaurantId = nextLunchRestaurantId;
    }
    public void setNextLunchRestaurantName(String nextLunchRestaurantName) {
        this.nextLunchRestaurantName = nextLunchRestaurantName;
    }
    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
    public void setLikedRestaurants(ArrayList<LikedRestaurant> likedRestaurants) {
        this.likedRestaurants = likedRestaurants;
    }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
    public void setIsNotified(Boolean isNotified) { this.isNotified = isNotified; }

}
