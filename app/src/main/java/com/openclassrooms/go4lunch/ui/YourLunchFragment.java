package com.openclassrooms.go4lunch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.FragmentYourLunchBinding;
import com.openclassrooms.go4lunch.helpers.CurrentUserSingleton;
import com.openclassrooms.go4lunch.models.User;

public class YourLunchFragment extends Fragment {

    private FragmentYourLunchBinding binding;

    public YourLunchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentYourLunchBinding.inflate(inflater, container, false);

        initUi();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initUi() {

        User user = CurrentUserSingleton.getInstance().getUser();

        binding.yourNextLunchTitle.setText(R.string.next_lunch_restaurant_base);
        binding.yourNextLunchName.setText(user.getNextLunchRestaurantName());
        binding.yourNextLunchName.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            intent.putExtra("restaurant_id", user.getNextLunchRestaurantId());
            v.getContext().startActivity(intent);
        });
        binding.yourFavoritesRestaurants.setText(R.string.favorites_restaurants_title);


        RecyclerView recyclerViewYourLunch = binding.restaurantsLikedRv;
        recyclerViewYourLunch.setAdapter(new YourLunchAdapter(
                user.getLikedRestaurants(), this.getContext()));
        recyclerViewYourLunch.addItemDecoration(new DividerItemDecoration(
                recyclerViewYourLunch.getContext(), DividerItemDecoration.VERTICAL));

    }
}