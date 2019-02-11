package com.example.lunchtimeboot.cycle.application;

import com.example.lunchtimeboot.infrastructure.ddd.CodedException;

import java.util.UUID;

public class CycleApplicationException extends CodedException {

    public static final String CYCLE_NOT_FOUND = "cycle_not_found";
    public static final String GET_USER_FAILED = "get_user_failed";

    private CycleApplicationException(String code, String message) {
        super(code, message);
    }

    static CycleApplicationException createCycleNotFound(UUID id) {
        String message = "Cycle ID:" + id + " is not found";

        CycleApplicationException exception = new CycleApplicationException(CYCLE_NOT_FOUND, message);

        return exception;
    }
}
