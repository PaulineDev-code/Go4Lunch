package com.openclassrooms.go4lunch.helpers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.models.User;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

import java.util.Objects;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static UserHelper USER_HELPER;


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
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String userEmail = user.getEmail();
            String username = user.getDisplayName();
            String uid = user.getUid();

            User userToCreate = new User(uid, username, userEmail, urlPicture, null);
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(userToCreate.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (!document.exists()) {
                            getUsersCollection().document(uid).set(userToCreate);
                        }
                    }
                }
            });
        }
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUser().getUid();

        return this.getUsersCollection().document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: You got the doc!");
                } else {
                    String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                    /*Log.e(TAG, "onComplete: fail getting document" + errorMessage, Exception)*/
                    Log.d(TAG, "onComplete: failed to get document" + errorMessage);
                }
            }
        });

    }

    //Get the list of users without the current user
    public Task<QuerySnapshot> getAllWorkmates() {

        return getUsersCollection().whereNotEqualTo(
                "uid", getCurrentUser().getUid()).get().addOnCompleteListener(
                    task -> {
                        if(task.isSuccessful()) { task.getResult(); }
                    }
        );
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }
}
