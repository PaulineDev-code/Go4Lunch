package com.openclassrooms.go4lunch.ui;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.ActivityDetailsBinding;
import com.openclassrooms.go4lunch.helpers.CurrentUserSingleton;
import com.openclassrooms.go4lunch.models.DetailsViewStateItem;
import com.openclassrooms.go4lunch.models.LikedRestaurant;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.service.MyFirebaseMessagingService;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;
import com.openclassrooms.go4lunch.viewmodels.ViewModelDetails;

import java.util.ArrayList;

public class DetailsActivity extends BaseActivity<ActivityDetailsBinding> {

    private String restaurantId;
    private ViewModelDetails viewModelDetails;
    private RecyclerView recyclerViewDetails;
    private CurrentUserSingleton userSingleton;
    private User user;
    private Drawable likeStarEmpty;
    private Drawable likeStarFull;

    @Override
    ActivityDetailsBinding getViewBinding() {
        return ActivityDetailsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                restaurantId = null;
            } else {
                restaurantId = extras.getString("restaurant_id");
            }
        } else {
            restaurantId = (String) savedInstanceState.getSerializable("restaurant_id");
        }

        userSingleton = CurrentUserSingleton.getInstance();
        user = userSingleton.getUser();
        if (user != null) {
            if (user.getNextLunchRestaurantId() == null || !user.getNextLunchRestaurantId().equals(restaurantId)) {
                binding.detailsChoiceButton.setImageResource(R.drawable.ic_baseline_add_24);
                binding.detailsChoiceButton.setColorFilter(getResources().getColor(R.color.orange_hard));
            } else if (user.getNextLunchRestaurantId().equals(restaurantId)) {
                binding.detailsChoiceButton.setImageResource(R.drawable.ic_baseline_check_circle_24);
                binding.detailsChoiceButton.setColorFilter(getResources().getColor(R.color.green_light));
            }
        }

        likeStarEmpty = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_star_purple500_24);
        likeStarFull = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_star_rate_24);

        viewModelDetails = new ViewModelProvider(this, ViewModelFactoryGo4Lunch.getInstance())
                .get(ViewModelDetails.class);
        viewModelDetails.initDetailsLiveData(restaurantId);
        viewModelDetails.getDetailsViewStateItem(restaurantId).observe(this, results -> {
            if (results != null) {
                if (results.getPhotoList() != null) {
                    Glide.with(this)
                            .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                    + results.getPhotoList().get(0).getPhotoReference()
                                    + "&key=" + BuildConfig.apiKey)
/*
                            .apply(new RequestOptions().centerCrop())
*/
                            .into(binding.restaurantDetailsPicture);
                } else {
                    binding.restaurantDetailsPicture.setImageResource(R.drawable.ic_baseline_no_photography_24);
                }
                binding.restaurantDetailsName.setText(results.getName());
                binding.restaurantDetailsAddress.setText(results.getAddress());
                if (results.getRating() != null) {
                    binding.restaurantDetailsRatingBar.setRating(results.getRating().floatValue() * 3 / 5);
                } else {
                    binding.restaurantDetailsRatingBar.setVisibility(View.INVISIBLE);
                }
                binding.restaurantDetailsPhone.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + results.getPhoneNumber().trim()));
                    startActivity(intent);
                });
                if(user.getLikedRestaurants() == null || user.getLikedRestaurants().isEmpty()) {
                    binding.restaurantDetailsLike.setCompoundDrawablesWithIntrinsicBounds(null, likeStarEmpty, null, null);
                } else {
                    for (LikedRestaurant restaurant : user.getLikedRestaurants()) {
                        if (restaurant.getPlaceId().equals(restaurantId)) {
                            binding.restaurantDetailsLike.setText(R.string.unlike_button);
                            binding.restaurantDetailsLike.setCompoundDrawablesWithIntrinsicBounds(null, likeStarFull, null, null);
                        } else {
                            binding.restaurantDetailsLike.setCompoundDrawablesWithIntrinsicBounds(null, likeStarEmpty, null, null);

                        }
                    }
                }

                binding.restaurantDetailsLike.setOnClickListener(v -> {
                    viewModelDetails.updateLikedRestaurants(results).observe(this, restaurantAdding -> {
                        if (restaurantAdding) {
                            binding.restaurantDetailsLike.setText(R.string.unlike_button);
                            binding.restaurantDetailsLike.setCompoundDrawablesWithIntrinsicBounds(null, likeStarFull, null, null);
                        } else {
                            binding.restaurantDetailsLike.setText(R.string.like_button);
                            binding.restaurantDetailsLike.setCompoundDrawablesWithIntrinsicBounds(null, likeStarEmpty, null, null);
                        }
                    });
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
                binding.detailsChoiceButton.setOnClickListener(v -> {
                    if (user != null) {
                        if (user.getNextLunchRestaurantId() == null) {
                            changeChosenRestaurant(results.getPlaceId(), results.getName());
                            binding.detailsChoiceButton.setImageResource(R.drawable.ic_baseline_check_circle_24);
                            binding.detailsChoiceButton.setColorFilter(getResources().getColor(R.color.green_light));
                        } else if (user.getNextLunchRestaurantId().equals(restaurantId)) {
                            changeChosenRestaurant(null, null);
                            binding.detailsChoiceButton.setImageResource(R.drawable.ic_baseline_add_24);
                        }
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

    /*private void likeRestaurant(DetailsViewStateItem restaurant) {
        ArrayList<DetailsViewStateItem> likedRestaurants;
        likedRestaurants = user.getLikedRestaurants();
        if (likedRestaurants == null) {
            likedRestaurants = new ArrayList<>();
        }
        if (!likedRestaurants.contains(restaurant)) {
            likedRestaurants.add(restaurant);
            user.setLikedRestaurants(likedRestaurants);
            userSingleton.setUser(user);
            viewModelDetails.updateLikedRestaurants(likedRestaurants);
            binding.restaurantDetailsLike.setBackgroundResource(R.drawable.ic_baseline_star_rate_24);
        } else {
            likedRestaurants.remove(restaurant);
            user.setLikedRestaurants(likedRestaurants);
            userSingleton.setUser(user);
            viewModelDetails.updateLikedRestaurants(likedRestaurants);
            binding.restaurantDetailsLike.setBackgroundResource(R.drawable.ic_baseline_star_purple500_24);
        }
    }*/

    private void changeChosenRestaurant(String Id, String Name) {
        user.setNextLunchRestaurantId(Id);
        user.setNextLunchRestaurantName(Name);
        userSingleton.setUser(user);
        viewModelDetails.updateChosenRestaurant(Id, Name);
    }



}
