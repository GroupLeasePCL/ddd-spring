package com.example.lunchtimeboot.cycle.event;

import com.example.lunchtimeboot.cycle.entity.Propose;
import com.example.lunchtimeboot.infrastructure.ddd.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.UUID;

public class ProposeMadeEvent extends Event {

    @JsonIgnore
    private Propose propose;
    private UUID proposeId;
    private UUID userId;
    private UUID restaurantId;
    private LocalDate forDate;

    public ProposeMadeEvent(Propose propose, UUID proposeId, UUID userId, UUID restaurantId, LocalDate forDate) {
        this.propose = propose;
        this.proposeId = proposeId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.forDate = forDate;
    }

    public Propose getPropose() {
        return propose;
    }

    public UUID getProposeId() {
        return proposeId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public LocalDate getForDate() {
        return forDate;
    }
}
