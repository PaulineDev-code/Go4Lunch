package com.openclassrooms.go4lunch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;

import com.openclassrooms.go4lunch.databinding.ActivityMain2Binding;
import com.openclassrooms.go4lunch.ui.drawerfragments.gallery.GalleryFragment;
import com.openclassrooms.go4lunch.BuildConfig;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;
    private BottomNavigationView bottomNavigationView;
    private static final String apiKey=BuildConfig.apiKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChatActivity();
            }
        });



        initDrawerNavigation();



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

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
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
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        return true;});

        bottomNavigationView = binding.appBarMain.bottomNavigationView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}