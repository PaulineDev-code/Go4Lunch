package com.openclassrooms.go4lunch.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.go4lunch.helpers.CurrentUserSingleton;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.repositories.UserRepository;

public class ViewModelSettings extends ViewModel {

    private final UserRepository userRepository;
    private MutableLiveData<Boolean> userNotification;

    public ViewModelSettings(@NonNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean updateUserNotifications(Boolean isNotified) {
        Boolean bool = null;
        CurrentUserSingleton userSingleton = CurrentUserSingleton.getInstance();
        User user = userSingleton.getUser();
        user.setIsNotified(isNotified);
        userNotification = userRepository.updateUserNotifications(isNotified);
        if(userNotification.getValue() != null && userNotification.getValue()) {
            userSingleton.setUser(user);
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }
}