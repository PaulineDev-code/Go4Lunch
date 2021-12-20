package com.openclassrooms.go4lunch.viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.repositories.UserRepository;

public class ViewModelSignIn extends ViewModel {

    private final UserRepository userRepository;

    public ViewModelSignIn(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public void createUser() {
        userRepository.createUser();
    }

    public Task<QuerySnapshot> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
