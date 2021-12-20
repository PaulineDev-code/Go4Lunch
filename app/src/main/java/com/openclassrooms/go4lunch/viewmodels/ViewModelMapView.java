package com.openclassrooms.go4lunch.viewmodels;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.LatLng;
import com.openclassrooms.go4lunch.models.maprestaurants.Result;
import com.openclassrooms.go4lunch.repositories.LocationRepository;
import com.openclassrooms.go4lunch.repositories.RestaurantRepository;
import com.openclassrooms.go4lunch.repositories.UserRepository;
import com.openclassrooms.go4lunch.utils.PermissionChecker;

import java.util.List;

public class ViewModelMapView extends ViewModel {

    @NonNull
    private final PermissionChecker permissionChecker;

    @NonNull
    private final LocationRepository locationRepository;
    private final RestaurantRepository restaurantRepository;
    private final LiveData<Location> locationLiveData;
    private MutableLiveData<List<Result>> resultRestaurantLiveData = new MutableLiveData<>();

    public ViewModelMapView(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull RestaurantRepository restaurantRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;

        locationLiveData = locationRepository.getLocationLiveData();
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        // No GPS permission
        if (!permissionChecker.hasLocationPermission()) {
            locationRepository.stopLocationRequest();
        } else {
            locationRepository.startLocationRequest();
        }
    }

    public LiveData<Location> getLocationLiveData() {
        return locationLiveData;
    }

    public LiveData<List<Result>> getRestaurantLiveData(String userLocation){
        return resultRestaurantLiveData = restaurantRepository.getNearbyRestaurants(userLocation);
    }


}
