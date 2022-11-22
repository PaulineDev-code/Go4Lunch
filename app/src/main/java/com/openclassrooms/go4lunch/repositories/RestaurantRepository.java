package com.openclassrooms.go4lunch.repositories;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.models.maprestaurants.PlacesPOJO;
import com.openclassrooms.go4lunch.models.maprestaurants.Result;
import com.openclassrooms.go4lunch.service.ApiInterface;
import com.openclassrooms.go4lunch.service.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    private static final String TYPE_KEYWORD = "restaurant";
    private final MutableLiveData<List<Result>> getNearbyRestaurants = new MutableLiveData<>();


    public MutableLiveData<List<Result>> getNearbyRestaurants(String location) {
        ApiInterface apiInterface = RetrofitService.getInterface();
        if (location != null) {
            Call<PlacesPOJO> nearbyRestaurants = apiInterface.getNearbyPlaces(
                    location, TYPE_KEYWORD, BuildConfig.apiKey
            );
            getNearbyRestaurants.setValue(new ArrayList<>());
            nearbyRestaurants.enqueue(new Callback<PlacesPOJO>() {
                @Override
                public void onResponse(@NonNull Call<PlacesPOJO> call,
                                       @NonNull Response<PlacesPOJO> response) {
                    if (response.body() != null) {
                        getNearbyRestaurants.setValue(response.body().getResults());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlacesPOJO> call, @NonNull Throwable t) {
                    Log.e("RestaurantRepository", "failed to load places", t);
                }
            });
        }
        return getNearbyRestaurants;
    }

    public MutableLiveData<List<Result>> getNearbyRestaurantsResults() {
        return getNearbyRestaurants;
    }

}
