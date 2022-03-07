package com.openclassrooms.go4lunch.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.RestaurantsListItemBinding;
import com.openclassrooms.go4lunch.databinding.WorkmatesItemBinding;
import com.openclassrooms.go4lunch.models.RestaurantViewStateItem;
import com.openclassrooms.go4lunch.models.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<RestaurantViewStateItem> mRestaurants;
    private final Context context;


    public ListAdapter(List<RestaurantViewStateItem> items, Context context) {
        this.mRestaurants = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListAdapter.MyViewHolder(RestaurantsListItemBinding.inflate(LayoutInflater.from(context),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder holder, int position) {
        RestaurantViewStateItem restaurant = mRestaurants.get(position);

        //Display restaurant's avatar
        if(restaurant.getPhotoList() != null){
            Glide.with(context)
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photoreference="
                +restaurant.getPhotoList().get(0).getPhotoReference()
                +"&key="+ BuildConfig.apiKey)
                .into(holder.binding.restaurantItemAvatar);}

        //Display restaurant's name, address & rating
        holder.binding.restaurantItemName.setText(restaurant.getName());
        holder.binding.restaurantItemAddress.setText(restaurant.getVicinity());
        holder.binding.restaurantItemRatingBar.setRating(restaurant.getRating().floatValue()*3/5);

        //Display workmates going to the restaurant
        if(restaurant.getWorkmates() != null && restaurant.getWorkmates() != 0) {
            holder.binding.restaurantItemWorkmatesGoing.setText(restaurant.getWorkmates());
        }
        else { holder.binding.restaurantItemWorkmatesGoing.setVisibility(View.INVISIBLE); }

        //Display Open/Close status
        if(restaurant.getOpen_now() == "Ouvert"){
            holder.binding.restaurantItemOpeningHours.setText(R.string.restaurant_open);
        }
        else if(restaurant.getOpen_now() == "Fermé") {
            holder.binding.restaurantItemOpeningHours.setText(R.string.restaurant_closed);
            holder.binding.restaurantItemOpeningHours.setTextColor(context.getResources().getColor(R.color.red));
        }
        else { holder.binding.restaurantItemOpeningHours.setText(R.string.restaurant_no_hours_info);}

        //Display distance between user and restaurant
        holder.binding.restaurantItemDistance.setText(restaurant.getDistance().intValue()+"m");
    }

    @Override
    public int getItemCount() {
        if(mRestaurants == null) {
            return 0;
        }
        else {
            return mRestaurants.size();
        }
    }

    void updateListWorkmates(@NonNull final List<RestaurantViewStateItem> restaurants) {
        this.mRestaurants = restaurants;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final RestaurantsListItemBinding binding;

        public MyViewHolder(RestaurantsListItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }



    }
}
