package com.openclassrooms.go4lunch.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.models.restaurantdetails.DetailsPOJO;
import com.openclassrooms.go4lunch.models.restaurantdetails.ResultDetails;
import com.openclassrooms.go4lunch.service.ApiInterface;
import com.openclassrooms.go4lunch.service.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsRepository {

    private final MutableLiveData<ResultDetails> restaurantDetailsResults = new MutableLiveData<>();
    private static final String API_FIELDS = "formatted_address,geometry,photos,vicinity,place_id,"+
            "name,rating,opening_hours,website,international_phone_number";


    public MutableLiveData<ResultDetails> getRestaurantDetails(String placeId){
        ApiInterface apiInterface = RetrofitService.getInterface();
        if(placeId != null){
            Call<DetailsPOJO> restaurantDetails = apiInterface.getDetailsRestaurant(
                    placeId, API_FIELDS, BuildConfig.apiKey
            );
            restaurantDetails.enqueue(new Callback<DetailsPOJO>() {
                @Override
                public void onResponse(@NonNull Call<DetailsPOJO> call, @NonNull Response<DetailsPOJO> response) {
                    if(response.body() != null) {
                        restaurantDetailsResults.setValue(response.body().getResult());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DetailsPOJO> call, @NonNull Throwable t) {
                    Log.e("DetailsRepository", "failed to load details", t);
                }
            });
        }
        return restaurantDetailsResults;
    }

    public MutableLiveData<ResultDetails> getRestaurantDetailsResults() {
        return restaurantDetailsResults;
    }

}
