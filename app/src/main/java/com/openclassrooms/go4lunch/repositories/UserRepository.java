package com.openclassrooms.go4lunch.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.helpers.UserHelper;
import com.openclassrooms.go4lunch.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {


    private final UserHelper userHelper = UserHelper.getInstance();
    private static UserRepository USER_REPOSITORY;
    private final MutableLiveData<List<User>> getAllWorkmates = new MutableLiveData<>();

    //Instance of Repository
    public static UserRepository getInstance() {
        if (USER_REPOSITORY == null) {
            USER_REPOSITORY = new UserRepository();
        }
        return USER_REPOSITORY;
    }


    // Create User in Firestore
    public void createUser() {
        userHelper.createUser();
    }

    public Task<Void> updateUserRestaurant(String placeId, String placeName) {
        return userHelper.updateUser(placeId, placeName);
    }

    public Task<User> getUserData(){
        // Get the user from Firestore and cast it to a User model Object
        return userHelper.getUserData().continueWith(new Continuation<DocumentSnapshot, User>() {
            @Override
            public User then(@NonNull @NotNull Task<DocumentSnapshot> task) throws Exception {
                return Objects.requireNonNull(task.getResult()).toObject(User.class);
            }
        });
    }

    //Get the list of users without the current user
    public MutableLiveData<List<User>> getWorkmates(){
        userHelper.getAllWorkmates().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<User> users = new ArrayList<>();
                    for( QueryDocumentSnapshot document : task.getResult()){
                        users.add(document.toObject(User.class));
                    }
                    getAllWorkmates.postValue(users);
                }
            }
        });
        return getAllWorkmates;
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

