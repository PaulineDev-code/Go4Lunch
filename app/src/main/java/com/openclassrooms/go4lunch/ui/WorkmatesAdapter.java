package com.openclassrooms.go4lunch.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.WorkmatesItemBinding;
import com.openclassrooms.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;


public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.MyViewHolder> {

    private List<User> mWorkmates;
    private final Context context;


    public WorkmatesAdapter(List<User> items, Context context) {
        this.mWorkmates = items;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(WorkmatesItemBinding.inflate(LayoutInflater.from(context),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User workmate = mWorkmates.get(position);
        Glide.with(context)
                .load(workmate.getUrlPicture())
                .apply(new RequestOptions().circleCrop())
                .into(holder.binding.workmatesItemAvatar);
        holder.binding.workmatesItemName.setText(workmate.getName());
        if(workmate.getNextLunchRestaurantName() == null) {
            holder.binding.workmatesItemRestaurantName.setText(R.string.no_restaurant_chosen);
        } else {
            holder.binding.workmatesItemRestaurantName.setText(workmate.getNextLunchRestaurantName());
        }

    }

    @Override
    public int getItemCount() {
        if(mWorkmates == null) {
            return 0;
        }
        else {
            return mWorkmates.size();
        }
    }

    void updateListWorkmates(@NonNull final List<User> workmates) {
        this.mWorkmates= workmates;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        /*private ImageView avatarWorkmate;
        private TextView nameWorkmate;
        private TextView nameRestaurant;*/

        private final WorkmatesItemBinding binding;

        public MyViewHolder(WorkmatesItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }



    }


}
