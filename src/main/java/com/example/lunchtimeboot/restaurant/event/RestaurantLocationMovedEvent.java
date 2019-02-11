package com.example.lunchtimeboot.restaurant.event;

import com.example.lunchtimeboot.infrastructure.ddd.Event;
import com.example.lunchtimeboot.restaurant.value.Location;

import java.util.UUID;

public class RestaurantLocationMovedEvent extends Event {

    private UUID restaurantId;
    private String restaurantName;
    private Location restaurantLocation;

    public RestaurantLocationMovedEvent(UUID restaurantId, String restaurantName, Location restaurantLocation) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Location getRestaurantLocation() {
        return restaurantLocation;
    }
}
