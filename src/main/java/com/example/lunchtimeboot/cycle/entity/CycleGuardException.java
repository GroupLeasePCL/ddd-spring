package com.example.lunchtimeboot.cycle.entity;

import com.example.lunchtimeboot.infrastructure.ddd.CodedException;
import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.entity.UserFavouriteRestaurant;

public class CycleGuardException extends CodedException {

    public static final String MEMBER_JOIN_ALREADY_JOINED = "member_join_already_joined";

    protected CycleGuardException(String code, String message) {
        super(code, message);
    }

    public static CycleGuardException createJoinAlreadyJoined(Cycle cycle, CycleMember cycleMember) {
        String message = String.format(
                "Cycle[%s]: User \"%s\" must not join already joined cycle \"%s\"",
                cycle.getId(),
                cycleMember.getName(),
                cycle.getName()
        );

        CycleGuardException exception = new CycleGuardException(MEMBER_JOIN_ALREADY_JOINED, message);

        return exception;
    }
}
