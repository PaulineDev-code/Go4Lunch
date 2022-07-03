package com.openclassrooms.go4lunch.ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;

import com.openclassrooms.go4lunch.databinding.ActivityMain2Binding;
import com.openclassrooms.go4lunch.repositories.UserRepository;
import com.openclassrooms.go4lunch.viewmodels.ViewModelMapView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;
    private BottomNavigationView bottomNavigationView;
    private static final String apiKey=BuildConfig.apiKey;
    private UserRepository userRepository;
    private ViewModelMapView viewModelMapView;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository();
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.apiKey, Locale.FRANCE);
        }

        setSupportActionBar(binding.appBarMainInclude.toolbar);
        binding.appBarMainInclude.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChatActivity();
            }
        });

        initDrawerNavigation();
        initViewModel();
        initAutoCompleteSearch();
    }

    private void startChatActivity() {
        Intent intentChatActivity = new Intent(this, ChatActivity.class);
        startActivity(intentChatActivity);
    }

    private void initDrawerNavigation() {
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapViewFragment, R.id.listViewFragment, R.id.workmatesFragment).setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);




        navigationView.setNavigationItemSelectedListener(item -> {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            finish();
        }
        else if (id == R.id.nav_gallery) {

            NavigationUI.onNavDestinationSelected(item, navController);
            drawer.closeDrawers();


        }
        else if (id == R.id.nav_slideshow) {
            userRepository.signOut(this).addOnSuccessListener(aVoid -> {finish();});
        }
        return true;});

        bottomNavigationView = binding.appBarMainInclude.bottomNavigationView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void initViewModel() {
        viewModelMapView = new ViewModelProvider(this).get(ViewModelMapView.class);
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0
        );
    }

    private void initAutoCompleteSearch() {
        binding.appBarMainInclude.toolbar.setOnClickListener(listener ->{
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });


    }


    /*public void loadFragment(Fragment fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame, fragment, null);
            transaction.remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main2)));
            *//*transaction.setReorderingAllowed(true);*//*
            transaction.commit();
        }*/







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}