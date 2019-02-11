package com.example.lunchtimeboot.user.application;

import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.repository.UserRepository;
import com.example.lunchtimeboot.user.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FavouriteRestaurantNotifierApplication {
    private UserRepository userRepository;
    private NotificationService notificationService;

    public FavouriteRestaurantNotifierApplication(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public void sendRestaurantMovedNotification(UUID restaurantId, String restaurantName) {
        /* validate */

        /* perform */
        List<User> restaurantFavedUsers = userRepository.findByFavouriteRestaurantId(restaurantId);

        restaurantFavedUsers.forEach(user -> {
            String message = String.format("[%s] Your favourite restaurant %s is moved", user.getName(), restaurantName);
            notificationService.sendNotification(message);
        });

        /* save */

        /* dispatch event */

    }
}
