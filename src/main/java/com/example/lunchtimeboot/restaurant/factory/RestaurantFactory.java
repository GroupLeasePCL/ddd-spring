package com.example.lunchtimeboot.restaurant.factory;

import com.example.lunchtimeboot.infrastructure.uuid.UuidGenerator;
import com.example.lunchtimeboot.restaurant.entity.Restaurant;
import com.example.lunchtimeboot.restaurant.value.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RestaurantFactory {

    private UuidGenerator uuidGenerator;

    @Autowired
    public RestaurantFactory(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    public Restaurant create(String name, Location location) {
        UUID id = this.uuidGenerator.createUuid4();

        Restaurant restaurant = new Restaurant(id, name, location);

        return restaurant;
    }
}
