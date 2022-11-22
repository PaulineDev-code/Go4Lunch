package com.openclassrooms.go4lunch.ui;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.go4lunch.databinding.FragmentListViewBinding;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;
import com.openclassrooms.go4lunch.viewmodels.ViewModelMapView;

public class ListViewFragment extends Fragment {

    private FragmentListViewBinding binding;
    private RecyclerView recyclerViewRestaurants;

    public ListViewFragment() {
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
        binding = FragmentListViewBinding.inflate(inflater, container, false);
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
        recyclerViewRestaurants = binding.restaurantsRv;
        ViewModelMapView viewModelMapView = new ViewModelProvider(
                requireActivity(), ViewModelFactoryGo4Lunch.getInstance())
                .get(ViewModelMapView.class);
        Location userLoc = viewModelMapView.getLocationLiveData().getValue();
        viewModelMapView.getRestaurantItemsLiveData(userLoc).observe(getViewLifecycleOwner(), results -> {
            if (results != null) {
                binding.restaurantsRv.setAdapter(new ListViewAdapter(results, requireContext()));
                recyclerViewRestaurants.addItemDecoration(new DividerItemDecoration(
                        recyclerViewRestaurants.getContext(), DividerItemDecoration.VERTICAL));
            }
        });

    }

}