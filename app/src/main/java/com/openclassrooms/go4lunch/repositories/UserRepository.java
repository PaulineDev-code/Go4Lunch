package com.openclassrooms.go4lunch.repositories;

import android.content.Context;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.models.User;

public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";



    // Get the Collection Reference
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if(user != null){
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String userEmail = user.getEmail();
            String username = user.getDisplayName();
            String uid = user.getUid();

            User userToCreate = new User(uid, username, userEmail, urlPicture);
            this.getUsersCollection().document().set(userToCreate);
        }
    }

    public Task<QuerySnapshot> getAllUsers() {
        return getUsersCollection().get();

    }

    @Nullable
    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

}

