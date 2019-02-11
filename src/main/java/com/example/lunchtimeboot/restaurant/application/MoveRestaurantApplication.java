package com.example.lunchtimeboot.restaurant.application;

import com.example.lunchtimeboot.infrastructure.activemq.Producer;
import com.example.lunchtimeboot.infrastructure.ddd.Event;
import com.example.lunchtimeboot.restaurant.entity.Restaurant;
import com.example.lunchtimeboot.restaurant.entity.RestaurantGuardException;
import com.example.lunchtimeboot.restaurant.event.Constant;
import com.example.lunchtimeboot.restaurant.repository.RestaurantRepository;
import com.example.lunchtimeboot.restaurant.value.Location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MoveRestaurantApplication {
    private ApplicationEventPublisher applicationEventPublisher;
    private Producer messageQueueProducer;
    private RestaurantRepository restaurantRepository;

    @Autowired
    public MoveRestaurantApplication(
            ApplicationEventPublisher applicationEventPublisher,
            Producer messageQueueProducer,
            RestaurantRepository restaurantRepository
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageQueueProducer = messageQueueProducer;
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant moveRestaurant(UUID restaurantId, Location newLocation) throws RestaurantApplicationException, RestaurantGuardException, JsonProcessingException {
        /* validate */

        /* perform */
        Optional<Restaurant> found = restaurantRepository.findById(restaurantId);

        if (!found.isPresent()) {
            throw RestaurantApplicationException.createRestaurantNotFound(restaurantId);
        }

        Restaurant restaurant = found.get().moveLocation(newLocation);

        /* save */
        restaurantRepository.save(restaurant);

        /* dispatch events */
        // local Event Dispatcher
        restaurant.getEvents().forEach(e -> applicationEventPublisher.publishEvent(e));

        // message queue
        ObjectMapper objectMapper = new ObjectMapper();
        for (Event e: restaurant.getEvents()) {
            String eventsJson = objectMapper.writeValueAsString(e);
            messageQueueProducer.publish(Constant.PRODUCER_DESTINATION_RESTAURANT, eventsJson);
            messageQueueProducer.publish(Constant.PRODUCER_DESTINATION_RESTAURANT_MOVED, eventsJson);
        }

        return restaurant;
    }
}
