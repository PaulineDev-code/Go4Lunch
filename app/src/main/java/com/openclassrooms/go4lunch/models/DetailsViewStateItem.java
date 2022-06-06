package com.openclassrooms.go4lunch.models;

import com.openclassrooms.go4lunch.models.maprestaurants.Photo;
import com.openclassrooms.go4lunch.models.restaurantdetails.OpeningHours;
import com.openclassrooms.go4lunch.models.restaurantdetails.ResultDetails;
import java.util.List;

public class DetailsViewStateItem {

    private String name;
    private String placeId;
    private String address;
    private List<Photo> photoList;
    private Double rating;
    private String vicinity;
    private OpeningHours openingHours;
    private String website;
    private String phoneNumber;
    private List<User> listWorkmatesGoing;

    public DetailsViewStateItem(
            String name, String placeId, String address, List<Photo> photoList,
            Double rating, String vicinity, OpeningHours openingHours, String website,
            String phoneNumber, List<User> listWorkmatesGoing) {

        this.name = name;
        this.placeId = placeId;
        this.address = address;
        this.photoList = photoList;
        this.rating = rating;
        this.vicinity = vicinity;
        this.openingHours = openingHours;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.listWorkmatesGoing = listWorkmatesGoing;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPlaceId() { return placeId; }

    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public List<Photo> getPhotoList() { return photoList; }

    public void setPhotoList(List<Photo> photoList) { this.photoList = photoList; }

    public Double getRating() { return rating; }

    public void setRating(Double rating) { this.rating = rating; }

    public String getVicinity() { return vicinity; }

    public void setVicinity(String vicinity) { this.vicinity = vicinity; }

    public OpeningHours getOpeningHours() { return openingHours; }

    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }

    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<User> getListWorkmatesGoing() { return listWorkmatesGoing; }

    public void setUidWorkmates(List<User> listWorkmatesGoing) {
        this.listWorkmatesGoing = listWorkmatesGoing;
    }

}
