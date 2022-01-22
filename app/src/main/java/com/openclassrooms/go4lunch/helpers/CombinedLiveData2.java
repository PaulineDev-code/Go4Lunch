package com.openclassrooms.go4lunch.helpers;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.openclassrooms.go4lunch.models.RestaurantViewStateItem;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.models.maprestaurants.Result;

import java.util.ArrayList;
import java.util.List;

public class CombinedLiveData2<A, B> extends MediatorLiveData<Pair<A, B>> {
    private A a;
    private B b;

    public CombinedLiveData2(LiveData<A> ld1, LiveData<B> ld2) {
        setValue(Pair.create(a, b));

        addSource(ld1, (a) -> {
            if(a != null) {
                this.a = a;
                /*if(b != null) {

                }*/
            }
            setValue(Pair.create(a, b));
        });

        addSource(ld2, (b) -> {
            if(b != null) {
                this.b = b;
            }
            setValue(Pair.create(a, b));
        });
    }
    /*private List<RestaurantViewStateItem> combineData (
            @NonNull List<Result> listRestaurants, @NonNull List<User> listWorkmates) {
        ArrayList<RestaurantViewStateItem> listRestaurantItems = new ArrayList<>();
        for (Result result : listRestaurants) {
            listRestaurantItems.add(new RestaurantViewStateItem(
                    result.getPlaceId(),
                    result.getName(),
                    result.getRating(),
                    result.getOpeningHours().getOpenNow(),
                    result.getVicinity(),
                    result.getGeometry().getLocation(),
                    result.getPhotos(),
                    computeWorkmatesGoing(result.getPlaceId(), listWorkmates)
            ));
        }
        return listRestaurantItems;
    }*/

    /*private Integer computeWorkmatesGoing(String placeId, @NonNull List<User> listWorkmates) {
        int workmates = 0;
        for (User result : listWorkmates) {
            if ((result.getNextLunchRestaurantId() != null) &&
                    (result.getNextLunchRestaurantId().equals(placeId))) {
                workmates++;
            }
        }

        return workmates;
    }*/

}
