package com.example.lunchtimeboot.user.application;

import com.example.lunchtimeboot.infrastructure.ddd.CodedException;

import java.util.UUID;

public class UserApplicationException extends CodedException {

    public static final String USER_NOT_FOUND = "user_not_found";

    private UserApplicationException(String code, String message) {
        super(code, message);
    }

    static UserApplicationException createUserNotFound(UUID id) {
        String message = "User ID:" + id + " is not found";

        UserApplicationException exception = new UserApplicationException(USER_NOT_FOUND, message);

        return exception;
    }
}
