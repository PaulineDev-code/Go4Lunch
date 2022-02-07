package com.openclassrooms.go4lunch.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.FragmentWorkmatesBinding;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;
import com.openclassrooms.go4lunch.viewmodels.ViewModelWorkmates;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;
    private ViewModelWorkmates viewModelWorkmates;
    @NonNull
    private RecyclerView recyclerViewWorkmates;
    @NonNull
    private List<User> listWorkmates;
    private WorkmatesAdapter adapter;

    public WorkmatesFragment() {
        // Required empty public constructor
    }


    public static WorkmatesFragment newInstance() {
        WorkmatesFragment fragment = new WorkmatesFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView (LayoutInflater inflater,
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
        recyclerViewWorkmates = binding.workmatesRv;
        viewModelWorkmates = new ViewModelProvider(
                this, ViewModelFactoryGo4Lunch.getInstance()).get(ViewModelWorkmates.class);
        viewModelWorkmates.getWorkmatesLiveData().observe(getViewLifecycleOwner(), results -> {
            if (results != null) {
                binding.workmatesRv.setAdapter(new WorkmatesAdapter(results, requireContext()));
            }
        });

    }



}