package com.openclassrooms.go4lunch.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.go4lunch.databinding.FragmentWorkmatesBinding;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;
import com.openclassrooms.go4lunch.viewmodels.ViewModelWorkmates;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;

    public WorkmatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initUI();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initUI() {
        ViewModelWorkmates viewModelWorkmates = new ViewModelProvider(
                this, ViewModelFactoryGo4Lunch.getInstance()).get(ViewModelWorkmates.class);
        viewModelWorkmates.getWorkmatesLiveData().observe(getViewLifecycleOwner(), results -> {
            if (results != null) {
                binding.workmatesRv.setAdapter(new WorkmatesAdapter(results, requireContext()));
            }
        });
        binding.fabMessage.setOnClickListener(view -> startChatActivity());

    }

    private void startChatActivity() {
        Intent intentChatActivity = new Intent(this.getContext(), ChatActivity.class);
        startActivity(intentChatActivity);
    }

}