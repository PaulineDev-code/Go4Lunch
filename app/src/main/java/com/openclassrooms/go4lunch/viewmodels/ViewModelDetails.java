package com.openclassrooms.go4lunch.viewmodels;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.openclassrooms.go4lunch.helpers.CombinedLiveData2;
import com.openclassrooms.go4lunch.helpers.CurrentUserSingleton;
import com.openclassrooms.go4lunch.models.DetailsViewStateItem;
import com.openclassrooms.go4lunch.models.LikedRestaurant;
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

    public void addLikedRestaurant(DetailsViewStateItem likedRestaurantDetails) {
        User user = CurrentUserSingleton.getInstance().getUser();
        LikedRestaurant likedRestaurant = new LikedRestaurant(likedRestaurantDetails.getName() ,
                likedRestaurantDetails.getPlaceId(), likedRestaurantDetails.getAddress(),
                likedRestaurantDetails.getPhotoList());
        ArrayList<LikedRestaurant> likedRestaurants;
        if(user.getLikedRestaurants() == null) {
            likedRestaurants = new ArrayList<>();
        } else {
            likedRestaurants = user.getLikedRestaurants();
        }
        likedRestaurants.add(likedRestaurant);
        user.setLikedRestaurants(likedRestaurants);
        CurrentUserSingleton.getInstance().setUser(user);
        userRepository.updateLikedRestaurants(likedRestaurants);
    }

    public void removeLikedRestaurant(DetailsViewStateItem unlikedRestaurant) {
        CurrentUserSingleton userSingleton = CurrentUserSingleton.getInstance();
        User user = userSingleton.getUser();
        ArrayList<LikedRestaurant> likedRestaurants = user.getLikedRestaurants();
        for(LikedRestaurant restaurant : likedRestaurants) {
            if(restaurant.getPlaceId().equals(unlikedRestaurant.getPlaceId())) {
                likedRestaurants.remove(restaurant);
            }
        }
        user.setLikedRestaurants(likedRestaurants);
        userSingleton.setUser(user);
        userRepository.updateLikedRestaurants(likedRestaurants);
    }

    public LiveData<Boolean> updateLikedRestaurants(DetailsViewStateItem likedRestaurantDetails) {
        CurrentUserSingleton userSingleton = CurrentUserSingleton.getInstance();
        User user = userSingleton.getUser();
        MutableLiveData<Boolean> likedRestaurantLiveData = new MutableLiveData<>();
        ArrayList<LikedRestaurant> restaurantsLiked;
        LikedRestaurant likedRestaurant = new LikedRestaurant(likedRestaurantDetails.getName() ,
                likedRestaurantDetails.getPlaceId(), likedRestaurantDetails.getAddress(),
                likedRestaurantDetails.getPhotoList());
        Boolean bool = null;
        if(user.getLikedRestaurants() != null && !user.getLikedRestaurants().isEmpty()) {
            restaurantsLiked = new ArrayList<>(user.getLikedRestaurants());
            for (LikedRestaurant restaurant :
                 restaurantsLiked) {
                if (restaurant.getPlaceId().equals(likedRestaurant.getPlaceId())) {
                    restaurantsLiked.remove(restaurant);
                    bool = false;
                } else {
                    restaurantsLiked.add(likedRestaurant);
                    bool = true;
                }
            }
        } else {
            restaurantsLiked = new ArrayList<>();
            restaurantsLiked.add(likedRestaurant);
            bool = true;
        }
        Boolean finalBool = bool;
        userRepository.updateLikedRestaurants(restaurantsLiked).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                user.setLikedRestaurants(restaurantsLiked);
                userSingleton.setUser(user);
                likedRestaurantLiveData.postValue(finalBool);
            }
        });
        return likedRestaurantLiveData;
    }


}
