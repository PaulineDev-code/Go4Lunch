package com.openclassrooms.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.go4lunch.helpers.CurrentUserSingleton;
import com.openclassrooms.go4lunch.repositories.UserRepository;

public class ViewModelSignIn extends ViewModel {

    private final UserRepository userRepository;

    public ViewModelSignIn(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    public LiveData<Boolean> getUser() {
        MutableLiveData<Boolean> userCreationLiveData = new MutableLiveData<>();
        if (CurrentUserSingleton.getInstance().getUser() == null) {
            userCreationLiveData = userRepository.getUser();
        } else {
            userCreationLiveData.postValue(true);
        }
        return userCreationLiveData;
    }

}
