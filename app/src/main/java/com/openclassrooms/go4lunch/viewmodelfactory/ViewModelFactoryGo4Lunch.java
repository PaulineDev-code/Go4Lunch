package com.openclassrooms.go4lunch.viewmodelfactory;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.go4lunch.repositories.UserRepository;
import com.openclassrooms.go4lunch.viewmodels.ViewModelGo4Lunch;

public class ViewModelFactoryGo4Lunch implements ViewModelProvider.Factory {
    private UserRepository userRepository = new UserRepository();


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelGo4Lunch.class)) {
            return (T) new ViewModelGo4Lunch(userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
