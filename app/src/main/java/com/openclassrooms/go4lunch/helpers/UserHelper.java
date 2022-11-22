package com.openclassrooms.go4lunch.helpers;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.models.LikedRestaurant;
import com.openclassrooms.go4lunch.models.User;

import java.util.ArrayList;
import java.util.Objects;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";
    private static UserHelper USER_HELPER;
    private final MutableLiveData<Boolean> createUserLiveData = new MutableLiveData<>();
    private static String FCM_TOKEN = "token";


    public static UserHelper getInstance() {
        if (USER_HELPER == null) {
            USER_HELPER = new UserHelper();
        }
        return USER_HELPER;
    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public MutableLiveData<Boolean> createUser() {
        FirebaseUser user = getCurrentUser();

        assert user != null;
        String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
        String userEmail = user.getEmail();
        String username = user.getDisplayName();
        String uid = user.getUid();

        User userToCreate = new User(uid, username, userEmail, urlPicture, null,
                null, new ArrayList<>(), FCM_TOKEN, true);
        getUsersCollection().document(uid).set(userToCreate).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                CurrentUserSingleton.getInstance().setUser(userToCreate);
                createUserLiveData.postValue(true);
            } else if (!task.isSuccessful()) {
                createUserLiveData.postValue(false);
            }
        });
        return createUserLiveData;
    }


    public Task<Void> updateUserChoice(String placeId, String placeName) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return FirebaseFirestore.getInstance().collection("users")
                    .document(user.getUid()).update("nextLunchRestaurantId", placeId,
                            "nextLunchRestaurantName", placeName);
        } else {
            return null;
        }
    }

    public Task<Void> updateLikedRestaurant(ArrayList<LikedRestaurant> restaurantsLiked) {
        User user = CurrentUserSingleton.getInstance().getUser();
        if (user != null) {
            return FirebaseFirestore.getInstance().collection("users")
                    .document(user.getUid()).update("likedRestaurants", restaurantsLiked);
        } else {
            return null;
        }
    }


    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData() {
        String uid = Objects.requireNonNull(this.getCurrentUser()).getUid();

        return this.getUsersCollection().document(uid).get();
    }

    //Get the list of users without the current user
    public Task<QuerySnapshot> getAllWorkmates() {

        return getUsersCollection().whereNotEqualTo(
                "uid", Objects.requireNonNull(getCurrentUser()).getUid()).get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                task.getResult();
                            }
                        }
                );
    }

    public Task<QuerySnapshot> getWorkmatesForRestaurant(String placeId) {

        return getUsersCollection().whereEqualTo(
                "nextLunchRestaurantId", Objects.requireNonNull(placeId)).get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                task.getResult();
                            }
                        }
                );
    }

    public void setFcmToken(String fcmToken) {
        FCM_TOKEN = fcmToken;
    }

    public Task<Void> updateUserNotifications(Boolean isNotified) {
        User user = CurrentUserSingleton.getInstance().getUser();
        if (user != null) {
            return FirebaseFirestore.getInstance().collection("users")
                    .document(user.getUid()).update("isNotified", isNotified);
        } else {
            return null;
        }
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }
}
