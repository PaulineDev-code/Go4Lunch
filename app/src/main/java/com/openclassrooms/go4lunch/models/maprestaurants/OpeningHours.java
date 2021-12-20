package com.openclassrooms.go4lunch.models.maprestaurants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OpeningHours implements Serializable
{

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    private final static long serialVersionUID = -4573078584808641547L;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

}
