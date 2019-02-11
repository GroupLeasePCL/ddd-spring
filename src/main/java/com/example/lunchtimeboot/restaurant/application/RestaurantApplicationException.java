package com.example.lunchtimeboot.restaurant.application;

import com.example.lunchtimeboot.infrastructure.ddd.CodedException;

import java.util.UUID;

public class RestaurantApplicationException extends CodedException {

    public static final String RESTAURANT_NOT_FOUND = "restaurant_not_found";

    private RestaurantApplicationException(String code, String message) {
        super(code, message);
    }

    static RestaurantApplicationException createRestaurantNotFound(UUID id) {
        String message = "Restaurant ID:" + id + " is not found";

        RestaurantApplicationException exception = new RestaurantApplicationException(RESTAURANT_NOT_FOUND, message);

        return exception;
    }
}
