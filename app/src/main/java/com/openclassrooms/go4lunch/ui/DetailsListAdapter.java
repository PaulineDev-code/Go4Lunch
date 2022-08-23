package com.openclassrooms.go4lunch.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.DetailsListWorkmatesItemBinding;
import com.openclassrooms.go4lunch.models.User;

import java.util.List;

public class DetailsListAdapter extends RecyclerView.Adapter<DetailsListAdapter.MyDetailsViewHolder> {

    private List<User> mWorkmates;
    private Context context;

    public DetailsListAdapter(List<User> workmatesList, Context context){
        this.mWorkmates = workmatesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyDetailsViewHolder(DetailsListWorkmatesItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsListAdapter.MyDetailsViewHolder holder, int position) {
        User workmate = mWorkmates.get(position);

        if(workmate.getUrlPicture()!=null) {
            Glide.with(context)
                    .load(workmate.getUrlPicture())
                    .into(holder.binding.detailsWorkmateAvatar);
        }

        String workmateJoining = workmate.getName().concat(" ")
                .concat(context.getResources().getString(R.string.is_joining));
        holder.binding.detailsWorkmateName.setText(workmateJoining);

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

    public static class MyDetailsViewHolder extends RecyclerView.ViewHolder {
        private final DetailsListWorkmatesItemBinding binding;

        public MyDetailsViewHolder(DetailsListWorkmatesItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
