package com.lwt.go4lunch.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantLatLngLiteral {

    @SerializedName("lat")
    @Expose
    private final Double lat;

    @SerializedName("lng")
    @Expose
    private final Double lng;

    public RestaurantLatLngLiteral(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() { return lat; }

    public Double getLng() { return lng; }
}
