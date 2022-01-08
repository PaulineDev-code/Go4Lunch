package com.openclassrooms.go4lunch.utils;

import android.app.Application;

public class MainApplication extends Application {
    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
    }

    public static Application getApplication() {
        return sApplication;
    }
}
