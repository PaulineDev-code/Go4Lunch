package com.openclassrooms.go4lunch.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.openclassrooms.go4lunch.databinding.ActivitySettingsBinding;

public class SettingsActivity extends BaseActivity<ActivitySettingsBinding>{

    @Override ActivitySettingsBinding getViewBinding() {
        return ActivitySettingsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

}
