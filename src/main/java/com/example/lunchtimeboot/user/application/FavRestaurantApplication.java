package com.example.lunchtimeboot.user.application;

import com.example.lunchtimeboot.infrastructure.activemq.Producer;
import com.example.lunchtimeboot.infrastructure.ddd.Event;
import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.entity.UserGuardException;
import com.example.lunchtimeboot.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FavRestaurantApplication {
    private ApplicationEventPublisher applicationEventPublisher;
    private Producer messageQueueProducer;
    private UserRepository userRepository;

    @Autowired
    public FavRestaurantApplication(
            ApplicationEventPublisher applicationEventPublisher,
            Producer messageQueueProducer,
            UserRepository userRepository
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageQueueProducer = messageQueueProducer;
        this.userRepository = userRepository;
    }

    public User favRestaurant(UUID userId, String restaurantName, UUID restaurantId) throws UserApplicationException, UserGuardException, JsonProcessingException {
        /* validate */

        /* perform */
        Optional<User> found = userRepository.findById(userId);
        if (!found.isPresent()) {
            throw UserApplicationException.createUserNotFound(userId);
        }
        User user = found.get();
        user.fav(restaurantName, restaurantId);

        /* save */
        userRepository.save(user);

        /* dispatch events */
        // local Event Dispatcher
        user.getEvents().forEach(e -> applicationEventPublisher.publishEvent(e));

        // message queue
        ObjectMapper objectMapper = new ObjectMapper();
        for (Event e : user.getEvents()) {
            String eventsJson = objectMapper.writeValueAsString(e);
            messageQueueProducer.publish(Constant.PRODUCER_DESTINATION_USER, eventsJson);
            messageQueueProducer.publish(Constant.PRODUCER_DESTINATION_USER_FAVED_RESTAURANT, eventsJson);
        }

        return user;
    }
}
