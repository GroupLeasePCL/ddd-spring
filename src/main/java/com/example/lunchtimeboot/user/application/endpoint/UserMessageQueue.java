package com.example.lunchtimeboot.user.application.endpoint;

import com.example.lunchtimeboot.infrastructure.client.restaurant.event.RestaurantLocationMovedEvent;
import com.example.lunchtimeboot.user.application.FavouriteRestaurantNotifierApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMessageQueue {
    private ObjectMapper objectMapper;
    private FavouriteRestaurantNotifierApplication favouriteRestaurantNotifierApplication;

    @Autowired
    public UserMessageQueue(ObjectMapper objectMapper, FavouriteRestaurantNotifierApplication favouriteRestaurantNotifierApplication) {
        this.objectMapper = objectMapper;
        this.favouriteRestaurantNotifierApplication = favouriteRestaurantNotifierApplication;
    }

    @JmsListener(destination = RestaurantLocationMovedEvent.PRODUCER_DESTINATION)
    public void sendFavouriteRestaurantMoved(String message) {
        try {
            RestaurantLocationMovedEvent event = objectMapper.readValue(message, RestaurantLocationMovedEvent.class);

            UUID restaurantId = event.getRestaurantId();
            String restaurantName = event.getRestaurantName();

            favouriteRestaurantNotifierApplication.sendRestaurantMovedNotification(restaurantId, restaurantName);
        } catch (Exception e) {
            // retry or put to dead queue
        }
    }
}
