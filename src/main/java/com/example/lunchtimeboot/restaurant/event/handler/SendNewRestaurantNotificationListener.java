package com.example.lunchtimeboot.restaurant.event.handler;

import com.example.lunchtimeboot.restaurant.event.RestaurantRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SendNewRestaurantNotificationListener {

    @EventListener
    public void sendNewRestaurantNotification(RestaurantRegisteredEvent event) {
        System.out.println("New restaurant available: " + event.getRestaurantName());
    }
}
