package com.example.lunchtimeboot.infrastructure.client.restaurant.event;

import com.example.lunchtimeboot.infrastructure.client.restaurant.value.Location;
import com.example.lunchtimeboot.infrastructure.ddd.Event;

import java.util.UUID;

public class RestaurantLocationMovedEvent extends Event {

    public static final String PRODUCER_DESTINATION = "restaurant:restaurant-moved";

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
