package com.openclassrooms.go4lunch.viewmodels;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.openclassrooms.go4lunch.helpers.CombinedLiveData2;
import com.openclassrooms.go4lunch.models.DetailsViewStateItem;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.models.restaurantdetails.ResultDetails;
import com.openclassrooms.go4lunch.repositories.DetailsRepository;
import com.openclassrooms.go4lunch.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewModelDetails extends ViewModel {

    private final DetailsRepository detailsRepository;
    private final UserRepository userRepository;

    public ViewModelDetails(@NonNull DetailsRepository detailsRepository,
                            @NonNull UserRepository userRepository) {
        this.detailsRepository = detailsRepository;
        this.userRepository = userRepository;

    }

    public LiveData<ResultDetails> initDetailsLiveData(String placeId) {
        return detailsRepository.getRestaurantDetails(placeId);
    }

    public LiveData<DetailsViewStateItem> getDetailsViewStateItem(String placeId) {
        LiveData<ResultDetails> detailsLiveData = detailsRepository.getRestaurantDetails(placeId);
        LiveData<List<User>> workmatesGoingLiveData = userRepository.getWorkmatesForRestaurant(
                placeId);
        CombinedLiveData2<ResultDetails, List<User>> combinedLiveData2 = new CombinedLiveData2<>(
                detailsLiveData, workmatesGoingLiveData);
        return Transformations.map(combinedLiveData2, myPair -> {
            if(myPair.first != null && myPair.second != null) {
                return new DetailsViewStateItem(
                        myPair.first.getName(),
                        myPair.first.getPlaceId(),
                        myPair.first.getFormattedAddress(),
                        myPair.first.getPhotos(),
                        myPair.first.getRating(),
                        myPair.first.getVicinity(),
                        myPair.first.getOpeningHours(),
                        myPair.first.getWebsite(),
                        myPair.first.getInternationalPhoneNumber(),
                        getListOfWorkmatesGoing(placeId, myPair.second)
                );
            } else { return null; }
        });
    }

    private List<User> getListOfWorkmatesGoing(String placeId, List<User> listWorkmates) {
        ArrayList<User> listWorkmatesGoing = new ArrayList<>();
        for (User result : listWorkmates) {
            if ((result.getNextLunchRestaurantId() != null) &&
                    (result.getNextLunchRestaurantId().equals(placeId))) {
            listWorkmatesGoing.add(result);
            }
        }
        return listWorkmatesGoing;
    }

    public Task<Void> updateChosenRestaurant(String restaurantId, String restaurantName) {
        return userRepository.updateUserRestaurant(restaurantId, restaurantName);
    }


}
