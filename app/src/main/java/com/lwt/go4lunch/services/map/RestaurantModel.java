package com.lwt.go4lunch.services.map;

public class RestaurantModel {
    public String name, address, distance, duration;

    public RestaurantModel(String name, String address, String distance, String duration) {

        this.name = name;
        this.address = address;
        this.distance = distance;
        this.duration = duration;
    }
}
