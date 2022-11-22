package com.openclassrooms.go4lunch.ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.ActivityMain2Binding;
import com.openclassrooms.go4lunch.helpers.CurrentUserSingleton;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.repositories.UserRepository;
import com.openclassrooms.go4lunch.viewmodels.ViewModelMapView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;
    private UserRepository userRepository;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = UserRepository.getInstance();
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.apiKey, Locale.FRANCE);
        }

        setSupportActionBar(binding.appBarMainInclude.toolbar);

        initDrawerNavigation();
        initViewModel();
        initAutoCompleteSearch();
    }

    private void initDrawerNavigation() {
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapViewFragment, R.id.listViewFragment, R.id.workmatesFragment)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(
                this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        User user = CurrentUserSingleton.getInstance().getUser();
        View headerView = navigationView.getHeaderView(0);
        ImageView navUserAvatar = (ImageView) headerView.findViewById(R.id.user_avatar);
        if (user.getUrlPicture() != null) {
            Glide.with(this)
                    .load(user.getUrlPicture())
                    .apply(new RequestOptions().circleCrop())
                    .into(navUserAvatar);
        } else {
            navUserAvatar.setImageResource(R.drawable.ic_baseline_no_photography_24);
        }
        TextView navUserName = (TextView) headerView.findViewById(R.id.user_name);
        navUserName.setText(user.getName());
        TextView navUserEmail = (TextView) headerView.findViewById(R.id.user_email);
        navUserEmail.setText(user.getEmail());

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.drawer_your_lunch) {
                NavigationUI.onNavDestinationSelected(item, navController);
                drawer.closeDrawers();
            } else if (id == R.id.drawer_settings) {

                NavigationUI.onNavDestinationSelected(item, navController);
                drawer.closeDrawers();

            } else if (id == R.id.drawer_sign_out) {
                userRepository.signOut(this).addOnSuccessListener(aVoid -> finish());
            }
            return true;
        });

        BottomNavigationView bottomNavigationView = binding.appBarMainInclude.bottomNavigationView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void initViewModel() {
        ViewModelMapView viewModelMapView = new ViewModelProvider(this)
                .get(ViewModelMapView.class);
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0
        );
    }

    private void initAutoCompleteSearch() {
        binding.appBarMainInclude.toolbar.setOnClickListener(listener -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("restaurant_id", place.getId());
                this.startActivity(intent);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                assert data != null;
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "Autocomplete result canceled");
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}