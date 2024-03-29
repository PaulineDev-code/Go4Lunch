package com.openclassrooms.go4lunch.utils;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class PermissionChecker {

    @NonNull
    private final Application application;

    public PermissionChecker(@NonNull Application application) {
        this.application = application;
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(application, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }
}
