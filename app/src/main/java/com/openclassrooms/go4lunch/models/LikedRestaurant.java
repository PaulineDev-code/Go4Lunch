package com.openclassrooms.go4lunch.models;

import com.openclassrooms.go4lunch.models.maprestaurants.Photo;

import java.util.List;

public class LikedRestaurant {
    private String name;
    private String placeId;
    private String address;
    private List<Photo> photoList;

    public LikedRestaurant() {
    }

    public LikedRestaurant(String name, String placeId, String address, List<Photo> photoList) {
        this.name = name;
        this.placeId = placeId;
        this.address = address;
        this.photoList = photoList;
    }

    public String getName() { return name; }

    public String getPlaceId() { return placeId; }

    public String getAddress() { return address; }

    public List<Photo> getPhotoList() { return photoList; }

    public void setName(String name) { this.name = name; }

    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public void setAddress(String address) { this.address = address; }

    public void setPhotoList(List<Photo> photoList) { this.photoList = photoList; }

}
