package com.openclassrooms.go4lunch.repositories;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.openclassrooms.go4lunch.helpers.CurrentUserSingleton;
import com.openclassrooms.go4lunch.helpers.UserHelper;
import com.openclassrooms.go4lunch.models.LikedRestaurant;
import com.openclassrooms.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {


    private final UserHelper userHelper = UserHelper.getInstance();
    private static UserRepository USER_REPOSITORY;
    private final MutableLiveData<User> createNewUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> getExistingUser = new MutableLiveData<>();
    private final MutableLiveData<List<User>> getAllWorkmates = new MutableLiveData<>();
    private final MutableLiveData<List<User>> workmatesForRestaurant = new MutableLiveData<>();

    //Instance of Repository
    public static UserRepository getInstance() {
        if (USER_REPOSITORY == null) {
            USER_REPOSITORY = new UserRepository();
        }
        return USER_REPOSITORY;
    }


    // Create User in Firestore
    public MutableLiveData<Boolean> createNewUser() {
        return userHelper.createUser();
    }

    public MutableLiveData<Boolean> getUser(){
        // Get the user from Firestore and cast it to a User model Object
        userHelper.getUserData().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if(task.getResult().exists()) {
                    CurrentUserSingleton.getInstance().setUser(task.getResult().toObject(User.class));
                    getExistingUser.postValue(true);
                } else {
                    MutableLiveData<Boolean> createUserLiveData = userHelper.createUser();
                    Transformations.map(createUserLiveData ,getBoolean -> {
                        getExistingUser.postValue(getBoolean);
                        return getExistingUser;
                    });
                }
            } else if(!task.isSuccessful()) {
                getExistingUser.postValue(false);
            }
        });
        return getExistingUser;
    }

    //Get the list of users without the current user
    public MutableLiveData<List<User>> getWorkmates(){
        userHelper.getAllWorkmates().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                ArrayList<User> users = new ArrayList<>();
                for( QueryDocumentSnapshot document : task.getResult()){
                    users.add(document.toObject(User.class));
                }
                getAllWorkmates.postValue(users);
            }
        });
        return getAllWorkmates;
    }

    public MutableLiveData<List<User>> getWorkmatesForRestaurant(String placeId) {
        userHelper.getWorkmatesForRestaurant(placeId).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                ArrayList<User> workmates = new ArrayList<>();
                for( QueryDocumentSnapshot document : task.getResult()) {
                    workmates.add(document.toObject(User.class));
                }
                workmatesForRestaurant.postValue(workmates);
            }
        });
        return workmatesForRestaurant;
    }

    public Task<Void> updateUserRestaurant(String placeId, String placeName) {
        return userHelper.updateUser(placeId, placeName);
    }

    public Task<Void> updateLikedRestaurants(ArrayList<LikedRestaurant> restaurantsLiked) {
        return userHelper.updateLikedRestaurant(restaurantsLiked);
    }

    public void setFcmToken(String fcmToken) {
        userHelper.setFcmToken(fcmToken);
    }

    @Nullable
    public FirebaseUser getCurrentUser(){
        return userHelper.getCurrentUser();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut(Context context){
        return userHelper.signOut(context);
    }

}

