package com.example.lunchtimeboot.restaurant.entity;

import com.example.lunchtimeboot.infrastructure.ddd.CodedException;
import com.example.lunchtimeboot.restaurant.value.Location;

public class RestaurantGuardException extends CodedException {

    public static final String MOVE_SAME_LOCATION = "move_same_location";

    private RestaurantGuardException(String code, String message) {
        super(code, message);
    }

    public static RestaurantGuardException createMoveSameLocation(Restaurant restaurant, Location newLocation) {
        String message = String.format(
                "Restaurant[%s]: Restaurant \"%s\" must not move to same location"
                        + "from lat=%s,lng=%s to lat=%s,lng%s",
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getLocation().getLatitude(),
                restaurant.getLocation().getLongitude(),
                newLocation.getLatitude(),
                newLocation.getLongitude()
        );

        RestaurantGuardException exception = new RestaurantGuardException(MOVE_SAME_LOCATION, message);

        return exception;
    }
}
