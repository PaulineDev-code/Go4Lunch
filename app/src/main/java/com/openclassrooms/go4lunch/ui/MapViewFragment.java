package com.openclassrooms.go4lunch.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.models.RestaurantViewStateItem;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;
import com.openclassrooms.go4lunch.viewmodels.ViewModelMapView;

import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private static final float INITIAL_ZOOM = 15;
    private ViewModelMapView viewModelMapView;
    private LatLng position;
    private String userLocation;
    private FloatingActionButton fab;

    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelMapView = new ViewModelProvider(requireActivity(),
                ViewModelFactoryGo4Lunch.getInstance()).get(ViewModelMapView.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        assert mapFragment != null;
        mapFragment.getMapAsync(MapViewFragment.this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private void startLocationUpdate() {
        viewModelMapView.getLocationLiveData().observe(getViewLifecycleOwner(), loc -> {
            /*currentLocation.set(loc);*/

            if (loc != null) {
                position = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, INITIAL_ZOOM));
                Log.i("géoloc", "startLocationUpdate: " + position);
                userLocation = position.latitude + "," + position.longitude;
                viewModelMapView.initRestaurantLiveData(userLocation);
                viewModelMapView.getRestaurantItemsLiveData(loc).observe(getViewLifecycleOwner(),
                        results -> {
                            if (results != null) {
                                addMarkers(results);
                            }
                        });
                if (mMap != null) {
                    mMap.addMarker(new MarkerOptions().position(position)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                            .title("Your location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    fab.setOnClickListener(v ->
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(position)));
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
        viewModelMapView.refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        startLocationUpdate();
    }

    private void addMarkers(List<RestaurantViewStateItem> restaurants) {
        for (RestaurantViewStateItem restaurant : restaurants) {
            if (restaurant.getWorkmates() > 0) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                restaurant.getLocation().getLat(),
                                restaurant.getLocation().getLng()))
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title(restaurant.getName())
                        .snippet(restaurant.getVicinity())
                );
            } else {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                restaurant.getLocation().getLat(),
                                restaurant.getLocation().getLng()))
                        .title(restaurant.getName())
                        .snippet(restaurant.getVicinity())
                );
            }
        }
    }
}