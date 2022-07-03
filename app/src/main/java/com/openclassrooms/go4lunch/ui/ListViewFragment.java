package com.openclassrooms.go4lunch.ui;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.go4lunch.databinding.FragmentListViewBinding;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;
import com.openclassrooms.go4lunch.viewmodels.ViewModelMapView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment {

    private FragmentListViewBinding binding;
    private ViewModelMapView viewModelMapView;
    @NonNull
    private RecyclerView recyclerViewRestaurants;
    @NonNull
    private List<User> listWorkmates;
    private ListViewAdapter adapter;

    public ListViewFragment() {
        // Required empty public constructor
    }


    public static ListViewFragment newInstance() {
        ListViewFragment fragment = new ListViewFragment();
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
        viewModelMapView = new ViewModelProvider(
                requireActivity(), ViewModelFactoryGo4Lunch.getInstance()).get(ViewModelMapView.class);
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