package com.openclassrooms.go4lunch.viewmodels;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.go4lunch.helpers.CombinedLiveData2;
import com.openclassrooms.go4lunch.models.RestaurantViewStateItem;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.models.maprestaurants.Result;
import com.openclassrooms.go4lunch.repositories.LocationRepository;
import com.openclassrooms.go4lunch.repositories.RestaurantRepository;
import com.openclassrooms.go4lunch.repositories.UserRepository;
import com.openclassrooms.go4lunch.utils.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

public class ViewModelMapView extends ViewModel {

    @NonNull
    private final PermissionChecker permissionChecker;

    @NonNull
    private final LocationRepository locationRepository;
    private final LiveData<Location> locationLiveData;
    private final CombinedLiveData2<List<Result>, List<User>> combinedLiveData2;
    private final RestaurantRepository restaurantRepository;

    public ViewModelMapView(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull RestaurantRepository restaurantRepository,
            @NonNull UserRepository userRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;

        locationLiveData = locationRepository.getLocationLiveData();
        LiveData<List<Result>> restaurantsLiveData =
                restaurantRepository.getNearbyRestaurantsResults();
        LiveData<List<User>> workmatesLiveData = userRepository.getWorkmates();

        combinedLiveData2 = new CombinedLiveData2<>(restaurantsLiveData, workmatesLiveData);
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

    public LiveData<List<RestaurantViewStateItem>> getRestaurantItemsLiveData(
            Location userLocation) {
        Location restaurantLocation = new Location("");
        return Transformations.map(combinedLiveData2, myPair -> {
            ArrayList<RestaurantViewStateItem> restaurantItems = new ArrayList<>();
            if (myPair.first != null && myPair.second != null) {
                for (Result result : myPair.first) {
                    restaurantLocation.setLatitude(result.getGeometry().getLocation().getLat());
                    restaurantLocation.setLongitude(result.getGeometry().getLocation().getLng());
                    restaurantItems.add(new RestaurantViewStateItem(
                            result.getPlaceId(),
                            result.getName(),
                            result.getRating(),
                            result.getOpeningHours(),
                            result.getVicinity(),
                            result.getGeometry().getLocation(),
                            result.getPhotos(),
                            computeWorkmatesGoing(result.getPlaceId(), myPair.second),
                            userLocation.distanceTo(restaurantLocation)
                    ));
                }
            }
            return restaurantItems;
        });
    }

    public void initRestaurantLiveData(@NonNull String userLocation) {
        restaurantRepository.getNearbyRestaurants(userLocation);
    }

    private Integer computeWorkmatesGoing(String placeId, @NonNull List<User> listWorkmates) {
        int workmates = 0;
        for (User result : listWorkmates) {
            if ((result.getNextLunchRestaurantId() != null) &&
                    (result.getNextLunchRestaurantId().equals(placeId))) {
                workmates++;
            }
        }
        return workmates;
    }
}
