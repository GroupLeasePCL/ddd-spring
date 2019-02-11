package com.example.lunchtimeboot.user.application;

import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.factory.UserFactory;
import com.example.lunchtimeboot.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserApplication {
    private ApplicationEventPublisher applicationEventPublisher;
    private UserFactory userFactory;
    private UserRepository userRepository;

    @Autowired
    public RegisterUserApplication(
            ApplicationEventPublisher applicationEventPublisher,
            UserFactory userFactory,
            UserRepository userRepository
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userFactory = userFactory;
        this.userRepository = userRepository;
    }

    public User registerNewUser(String name, String email, String mobile) {
        /* validate */

        /* perform */
        User user = this.userFactory.create(name, email, mobile);

        /* save */
        userRepository.save(user);

        /* dispatch events */
        user.getEvents().forEach(e -> applicationEventPublisher.publishEvent(e));

        return user;
    }
}
