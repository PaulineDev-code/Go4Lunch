package com.openclassrooms.go4lunch.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.RestaurantsListItemBinding;
import com.openclassrooms.go4lunch.models.RestaurantViewStateItem;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MyRestaurantViewHolder> {

    private List<RestaurantViewStateItem> mRestaurants;
    private final Context context;


    public ListViewAdapter(List<RestaurantViewStateItem> items, Context context) {
        this.mRestaurants = items;
        this.context = context;
    }

    @NonNull
    @Override
    public MyRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRestaurantViewHolder(RestaurantsListItemBinding.inflate(LayoutInflater.from(context),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRestaurantViewHolder holder, int position) {
        RestaurantViewStateItem restaurant = mRestaurants.get(position);

        //Display restaurant's avatar
        if(restaurant.getPhotoList() != null){
            Glide.with(context)
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photoreference="
                +restaurant.getPhotoList().get(0).getPhotoReference()
                +"&key="+ BuildConfig.apiKey)
                .into(holder.binding.restaurantItemAvatar);
        } else {
            holder.binding.restaurantItemAvatar.setImageResource(R.drawable.ic_baseline_no_photography_24);
        }

        //Display restaurant's name and address
        holder.binding.restaurantItemName.setText(restaurant.getName());
        holder.binding.restaurantItemAddress.setText(restaurant.getVicinity());

        //Display restaurant's rating
        if(restaurant.getRating() != null) {
        holder.binding.restaurantItemRatingBar.setRating(restaurant.getRating().floatValue()*3/5);
            }
        else { holder.binding.restaurantItemRatingBar.setVisibility(View.INVISIBLE);}

        //Display workmates going to the restaurant
        if(restaurant.getWorkmates() != null && restaurant.getWorkmates() != 0) {
            String nbWorkmates = ""+restaurant.getWorkmates();
            holder.binding.restaurantItemWorkmatesGoing.setText(nbWorkmates);
        }
        else { holder.binding.restaurantItemWorkmatesGoing.setVisibility(View.INVISIBLE); }

        //Display Open/Close status
        if(restaurant.getOpen_now().equals("Ouvert")){
            holder.binding.restaurantItemOpeningHours.setText(R.string.restaurant_open);
            holder.binding.restaurantItemOpeningHours.setTextColor
                    (context.getResources().getColor(R.color.green));
        }
        else if(restaurant.getOpen_now().equals("Fermé")) {
            holder.binding.restaurantItemOpeningHours.setText(R.string.restaurant_closed);
            holder.binding.restaurantItemOpeningHours.setTextColor
                    (context.getResources().getColor(R.color.red));
        }
        else { holder.binding.restaurantItemOpeningHours.setText(R.string.restaurant_no_hours_info);
            holder.binding.restaurantItemOpeningHours.setTextColor
                    (context.getResources().getColor(R.color.grey));
        }

        //Display distance between user and restaurant
        holder.binding.restaurantItemDistance.setText(restaurant.getDistance().intValue()+"m");

        //Open DetailsActivity when an item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            intent.putExtra("restaurant_id", restaurant.getPlaceId());
            v.getContext().startActivity(intent);
        });
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

    public static class MyRestaurantViewHolder extends RecyclerView.ViewHolder {

        private final RestaurantsListItemBinding binding;

        public MyRestaurantViewHolder(RestaurantsListItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
