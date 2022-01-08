package com.openclassrooms.go4lunch.ui.bottomfragments;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.models.maprestaurants.Result;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;
import com.openclassrooms.go4lunch.viewmodels.ViewModelMapView;
import com.openclassrooms.go4lunch.viewmodels.ViewModelSignIn;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private Location currentLocation ;
    private ViewModelFactoryGo4Lunch viewModelFactoryGo4Lunch;
    private ViewModelMapView viewModelMapView;
    private LatLng position ;
    private String userLocation;

    public MapViewFragment() {
        // Required empty public constructor
    }

    private MapViewFragment(Location location) {
        this.currentLocation = location;
    }

    public static MapViewFragment newInstance(Location location) {
        return new MapViewFragment(location);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelFactoryGo4Lunch = ViewModelFactoryGo4Lunch.getInstance();
        viewModelMapView = viewModelFactoryGo4Lunch.create(ViewModelMapView.class);
        ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0
        );
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(MapViewFragment.this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        /*String userLocation = position.latitude+","+position.longitude;
        viewModelMapView.getRestaurantLiveData(userLocation).observe(getViewLifecycleOwner(), results -> {
            if(results != null){
                addMarkers(results);
            }
        });*/
    }

    /*private void setPosition(Location location) {
        this.position = new LatLng(location.getLatitude(), location.getLongitude());
    }*/

    private void startLocationUpdate(){
        viewModelMapView.getLocationLiveData().observe(getViewLifecycleOwner(), loc -> {
            /*currentLocation.set(loc);*/

            if(loc != null){
                position = new LatLng(loc.getLatitude(),loc.getLongitude());
                Log.i("géoloc", "startLocationUpdate: "+position);
                userLocation = position.latitude+","+position.longitude;
                viewModelMapView.getRestaurantLiveData(userLocation).observe(getViewLifecycleOwner(), results -> {
                    if(results != null){
                        addMarkers(results);
                    }
                });
                if(mMap != null) {
                    mMap.addMarker(new MarkerOptions().position(position).title("Marker: Your location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
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
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        startLocationUpdate();
        // Add a marker in Sydney and move the camera
        /*mMap.addMarker(new MarkerOptions().position(position).title("Marker in Sydney"));*/
        /*mMap.moveCamera(CameraUpdateFactory.newLatLng(position));*/
    }

    private void addMarkers(List<Result> restaurants){
        for (Result restaurant : restaurants){
            mMap.addMarker(new MarkerOptions()
            .position(new LatLng(
                    restaurant.getGeometry().getLocation().getLat(),
                    restaurant.getGeometry().getLocation().getLng()))
            .title(restaurant.getName())
            .snippet(restaurant.getVicinity())
            );
        }
    }
}