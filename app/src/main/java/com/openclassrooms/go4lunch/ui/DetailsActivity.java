package com.openclassrooms.go4lunch.ui;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.ActivityDetailsBinding;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;
import com.openclassrooms.go4lunch.viewmodels.ViewModelDetails;

public class DetailsActivity extends BaseActivity<ActivityDetailsBinding>{

    private String restaurantId;
    private ViewModelDetails viewModelDetails;
    private RecyclerView recyclerViewDetails;

    @Override
    ActivityDetailsBinding getViewBinding() {
        return ActivityDetailsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                restaurantId = null;
            } else {
                restaurantId = extras.getString("restaurant_id");
            }
        } else {
            restaurantId = (String) savedInstanceState.getSerializable("restaurant_id");
        }

        viewModelDetails = new ViewModelProvider(this, ViewModelFactoryGo4Lunch.getInstance())
                .get(ViewModelDetails.class);
        viewModelDetails.initDetailsLiveData(restaurantId);
        viewModelDetails.getDetailsViewStateItem(restaurantId).observe(this, results -> {
            if(results != null) {
                if(results.getPhotoList() != null){
                    Glide.with(this)
                            .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                    +results.getPhotoList().get(0).getPhotoReference()
                                    +"&key="+ BuildConfig.apiKey)
/*
                            .apply(new RequestOptions().centerCrop())
*/
                            .into(binding.restaurantDetailsPicture);}
                binding.restaurantDetailsName.setText(results.getName());
                binding.restaurantDetailsAddress.setText(results.getAddress());
                if(results.getRating() != null) {
                    binding.restaurantDetailsRatingBar.setRating(results.getRating().floatValue()*3/5);
                    LayerDrawable layerDrawable =
                            (LayerDrawable) binding.restaurantDetailsRatingBar.getProgressDrawable();
                    DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)),
                            getResources().getColor(R.color.grey_light));
                    DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(1)),
                            getResources().getColor(R.color.yellow_dusk));
                    DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)),
                            getResources().getColor(R.color.yellow_dusk));
                }
                else { binding.restaurantDetailsRatingBar.setVisibility(View.INVISIBLE);}
                binding.restaurantDetailsPhone.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + results.getPhoneNumber().trim()));
                    startActivity(intent);
                });
                binding.restaurantDetailsWebsite.setOnClickListener(v -> {
                    if (results.getWebsite() == null) {
                        Toast toast = Toast.makeText(this, R.string.no_website, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        Intent openURL = new Intent(Intent.ACTION_VIEW);
                        openURL.setData(Uri.parse(results.getWebsite()));
                        startActivity(openURL);
                    }
                });
                recyclerViewDetails = binding.restaurantDetailsWorkmatesRv;
                recyclerViewDetails.setAdapter(new DetailsListAdapter(
                        results.getListWorkmatesGoing(), DetailsActivity.this));
                recyclerViewDetails.addItemDecoration(new DividerItemDecoration(
                        recyclerViewDetails.getContext(), DividerItemDecoration.VERTICAL));
            }
        });
    }

}
