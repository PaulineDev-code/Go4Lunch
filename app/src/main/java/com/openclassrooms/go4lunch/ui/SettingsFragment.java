package com.openclassrooms.go4lunch.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.go4lunch.databinding.FragmentSettingsBinding;
import com.openclassrooms.go4lunch.helpers.CurrentUserSingleton;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;
import com.openclassrooms.go4lunch.viewmodels.ViewModelSettings;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private ViewModelSettings viewModelSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelSettings = new ViewModelProvider(requireActivity(),
                ViewModelFactoryGo4Lunch.getInstance()).get(ViewModelSettings.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initUI();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initUI() {

        Boolean isNotified = CurrentUserSingleton.getInstance().getUser().getIsNotified();

        binding.settingsSwitchButton.setChecked(isNotified);

        binding.settingsSwitchButton.setOnClickListener(v -> {
            boolean newNotified = !CurrentUserSingleton.getInstance().getUser().getIsNotified();
            Boolean notificationChangesResult;
            notificationChangesResult = viewModelSettings.updateUserNotifications(newNotified);
            if (notificationChangesResult) {
                binding.settingsSwitchButton.setChecked(newNotified);
            } else {
                Toast.makeText(this.getContext(),
                        "Error occurred, please try again", Toast.LENGTH_SHORT).show();
            }

        });
    }
}