package com.openclassrooms.go4lunch.helpers;

import com.openclassrooms.go4lunch.models.User;

public class CurrentUserSingleton {
    User user;

    private static CurrentUserSingleton userSingleton;

    public static CurrentUserSingleton getInstance() {
        if (userSingleton == null) {
            userSingleton = new CurrentUserSingleton();
        }
        return userSingleton;
    }

    public CurrentUserSingleton() {}

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
