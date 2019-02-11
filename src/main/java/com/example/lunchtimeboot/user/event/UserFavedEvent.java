package com.example.lunchtimeboot.user.event;

import com.example.lunchtimeboot.infrastructure.ddd.Event;

import java.util.UUID;

public class UserFavedEvent extends Event {

    private UUID userId;
    private String userName;
    private UUID userFavouriteRestaurantId;

    public UserFavedEvent(UUID userId, String userName, UUID restaurantId) {
        this.userId = userId;
        this.userName = userName;
        this.userFavouriteRestaurantId = restaurantId;
    }

    public String getUserName() {
        return userName;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getUserFavouriteRestaurantId() {
        return userFavouriteRestaurantId;
    }
}
