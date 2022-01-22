package com.openclassrooms.go4lunch.models;

import com.openclassrooms.go4lunch.models.maprestaurants.Location;
import com.openclassrooms.go4lunch.models.maprestaurants.OpeningHours;
import com.openclassrooms.go4lunch.models.maprestaurants.Photo;

import java.util.List;

public class RestaurantViewStateItem {

    private String placeId;
    private String name;
    private Double rating;
    private String open_now;
    private String vicinity;
    private Location location;
    private List<Photo> photoList;
    private Integer workmates;

    public RestaurantViewStateItem(
            String placeId, String name, Double rating, OpeningHours open_now,
            String vicinity, Location location, List<Photo> photoList, Integer workmates) {

        this.placeId = placeId;
        this.name = name;
        this.rating = rating;
        this.open_now = open_now == null? "Pas d'infos" : (open_now.getOpenNow()?"Ouvert":"Fermé");
        this.vicinity = vicinity;
        this.location = location;
        this.photoList = photoList;
        this.workmates = workmates;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getOpen_now() {
        return open_now;
    }

    public void setOpen_now(String open_now) {
        this.open_now = open_now;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public Integer getWorkmates() {
        return workmates;
    }

    public void setWorkmates(Integer workmates) {
        this.workmates = workmates;
    }
}
