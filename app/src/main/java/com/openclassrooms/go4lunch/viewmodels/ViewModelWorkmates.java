package com.openclassrooms.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.repositories.UserRepository;

import java.util.List;

public class ViewModelWorkmates extends ViewModel {

    private final UserRepository userRepository;

    public ViewModelWorkmates(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public LiveData<List<User>> getWorkmatesLiveData() {
        return userRepository.getWorkmates();
    }


}
