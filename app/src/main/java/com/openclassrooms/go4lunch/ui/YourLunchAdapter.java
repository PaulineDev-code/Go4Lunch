package com.openclassrooms.go4lunch.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.databinding.RestaurantsLikedItemBinding;
import com.openclassrooms.go4lunch.models.LikedRestaurant;

import java.util.List;

public class YourLunchAdapter extends RecyclerView.Adapter<YourLunchAdapter.MyLunchViewHolder> {

    private final List<LikedRestaurant> mRestaurants;
    private final Context context;

    public YourLunchAdapter(List<LikedRestaurant> restaurantsList, Context context) {
        this.mRestaurants = restaurantsList;
        this.context = context;
    }

    @NonNull
    @Override
    public YourLunchAdapter.MyLunchViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        return new YourLunchAdapter.MyLunchViewHolder(RestaurantsLikedItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull YourLunchAdapter.MyLunchViewHolder holder, int position) {
        LikedRestaurant restaurant = mRestaurants.get(position);

        if (restaurant.getPhotoList().get(0) != null) {
            Glide.with(context)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photoreference="
                            + restaurant.getPhotoList().get(0).getPhotoReference()
                            + "&key=" + BuildConfig.apiKey)
                    .apply(new RequestOptions().circleCrop())
                    .into(holder.binding.restaurantItemPhoto);
        }

        holder.binding.restaurantItemName.setText(restaurant.getName());
        holder.binding.restaurantItemAddress.setText(restaurant.getAddress());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            intent.putExtra("restaurant_id", restaurant.getPlaceId());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (mRestaurants == null) {
            return 0;
        } else {
            return mRestaurants.size();
        }
    }

    public static class MyLunchViewHolder extends RecyclerView.ViewHolder {
        private final RestaurantsLikedItemBinding binding;

        public MyLunchViewHolder(RestaurantsLikedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
