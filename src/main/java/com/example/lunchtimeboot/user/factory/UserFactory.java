package com.example.lunchtimeboot.user.factory;

import com.example.lunchtimeboot.infrastructure.uuid.UuidGenerator;
import com.example.lunchtimeboot.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserFactory {

    private UuidGenerator uuidGenerator;

    @Autowired
    public UserFactory(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    public User create(String name, String email, String mobile) {
        UUID id = this.uuidGenerator.createUuid4();

        User user = new User(id, name, email, mobile);

        return user;
    }
}
