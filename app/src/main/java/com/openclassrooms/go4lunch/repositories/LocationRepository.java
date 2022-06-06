package com.openclassrooms.go4lunch.repositories;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnFailureListener;

public class LocationRepository {
    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;

    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;

    @NonNull
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>(null);

    private LocationCallback callback;

    public LocationRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    public LiveData<Location> getLocationLiveData() {
        return locationMutableLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();

                        locationMutableLiveData.setValue(location);

                }
            };
        }

        /*fusedLocationProviderClient.removeLocationUpdates(callback);*/

        fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                        .setInterval(LOCATION_REQUEST_INTERVAL_MS),
                callback,
                Looper.getMainLooper()
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("", e.getMessage());
            }
        });
    }

    public void stopLocationRequest() {
        if (callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }

}
