package com.example.lunchtimeboot.restaurant.application;

import com.example.lunchtimeboot.restaurant.entity.Restaurant;
import com.example.lunchtimeboot.restaurant.factory.RestaurantFactory;
import com.example.lunchtimeboot.restaurant.repository.RestaurantRepository;
import com.example.lunchtimeboot.restaurant.value.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class RegisterNewRestaurantApplication {
    private ApplicationEventPublisher applicationEventPublisher;
    private RestaurantFactory restaurantFactory;
    private RestaurantRepository restaurantRepository;

    @Autowired
    public RegisterNewRestaurantApplication(
            ApplicationEventPublisher applicationEventPublisher,
            RestaurantFactory restaurantFactory,
            RestaurantRepository restaurantRepository
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.restaurantFactory = restaurantFactory;
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant registerNewRestaurant(String name, Location location) {
        /* validate */

        /* perform */
        Restaurant restaurant = this.restaurantFactory.create(name, location);

        /* save */
        restaurantRepository.save(restaurant);

        /* dispatch events */
        restaurant.getEvents().forEach(e -> applicationEventPublisher.publishEvent(e));

        return restaurant;
    }
}
