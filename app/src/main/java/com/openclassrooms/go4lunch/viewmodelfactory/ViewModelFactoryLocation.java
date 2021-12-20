/*
package com.openclassrooms.go4lunch.viewmodelfactory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.openclassrooms.go4lunch.repositories.LocationRepository;
import com.openclassrooms.go4lunch.utils.MainApplication;
import com.openclassrooms.go4lunch.utils.PermissionChecker;
import com.openclassrooms.go4lunch.viewmodels.ViewModelMapView;

public class ViewModelFactoryLocation implements ViewModelProvider.Factory {
    private volatile static ViewModelFactoryLocation sInstance;

    @NonNull
    private final PermissionChecker permissionChecker;

    @NonNull
    private final LocationRepository locationRepository;

    public static ViewModelFactoryLocation getInstance() {
        if (sInstance == null) {
            // Double Checked Locking singleton pattern with Volatile works on Android since Honeycomb
            synchronized (ViewModelFactoryLocation.class) {
                if (sInstance == null) {
                    Application application = MainApplication.getApplication();

                    sInstance = new ViewModelFactoryLocation(
                            new PermissionChecker(
                                    application
                            ),
                            new LocationRepository(
                                    LocationServices.getFusedLocationProviderClient(
                                            application
                                    )
                            )
                    );
                }
            }
        }

        return sInstance;
    }

    private ViewModelFactoryLocation(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository
    ) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelMapView.class)) {
            return (T) new ViewModelMapView(
                    permissionChecker,
                    locationRepository
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }
}*/
