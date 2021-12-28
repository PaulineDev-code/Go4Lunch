package com.openclassrooms.go4lunch.service;

import com.openclassrooms.go4lunch.models.maprestaurants.PlacesPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/maps/api/place/nearbysearch/json")
    Call<PlacesPOJO> getNearbyPlaces(
            @Query("location") String location, @Query("radius") int radius,
            @Query("type") String type, @Query("key") String key
    );
}
