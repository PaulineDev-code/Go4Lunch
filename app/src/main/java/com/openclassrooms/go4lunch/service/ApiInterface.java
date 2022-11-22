package com.openclassrooms.go4lunch.service;

import com.openclassrooms.go4lunch.models.maprestaurants.PlacesPOJO;
import com.openclassrooms.go4lunch.models.restaurantdetails.DetailsPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/maps/api/place/nearbysearch/json?rankby=distance")
    Call<PlacesPOJO> getNearbyPlaces(
            @Query("location") String location, @Query("type") String type, @Query("key") String key
    );

    @GET("/maps/api/place/details/json")
    Call<DetailsPOJO> getDetailsRestaurant(
            @Query("place_id") String place_id, @Query("fields") String fields, @Query("key") String key
    );

}
