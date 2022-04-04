package com.openclassrooms.go4lunch.viewmodelfactory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.openclassrooms.go4lunch.repositories.DetailsRepository;
import com.openclassrooms.go4lunch.repositories.LocationRepository;
import com.openclassrooms.go4lunch.repositories.RestaurantRepository;
import com.openclassrooms.go4lunch.repositories.UserRepository;
import com.openclassrooms.go4lunch.utils.MainApplication;
import com.openclassrooms.go4lunch.utils.PermissionChecker;
import com.openclassrooms.go4lunch.viewmodels.ViewModelDetails;
import com.openclassrooms.go4lunch.viewmodels.ViewModelMapView;
import com.openclassrooms.go4lunch.viewmodels.ViewModelSignIn;
import com.openclassrooms.go4lunch.viewmodels.ViewModelWorkmates;

public class ViewModelFactoryGo4Lunch implements ViewModelProvider.Factory {

    private volatile static ViewModelFactoryGo4Lunch sInstance;

    private final UserRepository userRepository = new UserRepository();

    @NonNull
    private final PermissionChecker permissionChecker;

    @NonNull
    private final LocationRepository locationRepository;

    @NonNull
    private final  RestaurantRepository restaurantRepository = new RestaurantRepository();

    @NonNull
    private final DetailsRepository detailsRepository = new DetailsRepository();



    public static ViewModelFactoryGo4Lunch getInstance() {
        if (sInstance == null) {
            // Double Checked Locking singleton pattern with Volatile works on Android since Honeycomb
            synchronized (ViewModelFactoryGo4Lunch.class) {
                if (sInstance == null) {
                    Application application = MainApplication.getApplication();

                    sInstance = new ViewModelFactoryGo4Lunch(
                            new PermissionChecker(
                                    application
                            ),
                            new LocationRepository(
                                    LocationServices.getFusedLocationProviderClient(
                                            application
                                    )
                            ),
                            new RestaurantRepository()
                    );
                }
            }
        }

        return sInstance;
    }

    private ViewModelFactoryGo4Lunch(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull RestaurantRepository restaurantRepository
            ) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelSignIn.class)) {
            return (T) new ViewModelSignIn(userRepository);
        }
        else if (modelClass.isAssignableFrom(ViewModelMapView.class)){
            return (T) new ViewModelMapView(
                    permissionChecker,
                    locationRepository,
                    restaurantRepository,
                    userRepository
            );
        }
        else if (modelClass.isAssignableFrom(ViewModelWorkmates.class)){
            return (T) new ViewModelWorkmates(userRepository);
        }
        else if (modelClass.isAssignableFrom(ViewModelDetails.class)){
            return (T) new ViewModelDetails(detailsRepository, userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class" + modelClass);
    }
}
