package com.example.lunchtimeboot.user.entity;

import com.example.lunchtimeboot.infrastructure.ddd.CodedException;

public class UserGuardException extends CodedException {

    public static final String FAV_ALREADY_FAVED_RESTAURANT = "fav_already_faved_restaurant";

    protected UserGuardException(String code, String message) {
        super(code, message);
    }

    public static UserGuardException createFavAlreadyFavedRestaurant(User user, UserFavouriteRestaurant favouriteRestaurant) {
        String message = String.format(
                "User[%s]: User \"%s\" must not fav already faved restaurant \"%s\"",
                user.getId(),
                user.getName(),
                favouriteRestaurant.getName()
        );

        UserGuardException exception = new UserGuardException(FAV_ALREADY_FAVED_RESTAURANT, message);

        return exception;
    }
}
