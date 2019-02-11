package com.example.lunchtimeboot.cycle.application;

import com.example.lunchtimeboot.infrastructure.ddd.CodedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ProposeApplicationException extends CodedException {

    public static final String PROPOSE_BACK_DATE = "propose_back_date";
    public static final String PROPOSED_TO_GIVEN_DATE = "proposed_to_given_date";

    private ProposeApplicationException(String code, String message) {
        super(code, message);
    }

    static ProposeApplicationException proposeBackDate(UUID userId, UUID restaurantId, LocalDate forDate) {
        String message = String.format(
                "Propose: Backdate propose (%s) must not be made by user:%s, restaurant:%s",
                forDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                userId.toString(),
                restaurantId
        );

        ProposeApplicationException exception = new ProposeApplicationException(PROPOSE_BACK_DATE, message);

        return exception;
    }

    static ProposeApplicationException proposedForGivenDate(UUID userId, UUID proposedRestaurantId, LocalDate forDate) {
        String message = String.format(
                "Propose: User id:%s already proposed restaurant id:%s for %s",
                userId.toString(),
                proposedRestaurantId.toString(),
                forDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        );

        ProposeApplicationException exception = new ProposeApplicationException(PROPOSE_BACK_DATE, message);

        return exception;
    }
}
