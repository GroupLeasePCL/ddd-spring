package com.example.lunchtimeboot.cycle.event;

import com.example.lunchtimeboot.cycle.entity.Cycle;
import com.example.lunchtimeboot.infrastructure.ddd.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class CycleJoinedEvent extends Event {
    @JsonIgnore
    private Cycle cycle;
    private UUID cycleId;
    private String cycleName;
    private UUID userId;
    private String userName;

    public CycleJoinedEvent(Cycle cycle, UUID cycleId, String cycleName, String userName, UUID userId) {
        this.cycle = cycle;
        this.cycleId = cycleId;
        this.cycleName = cycleName;
        this.userId = userId;
        this.userName = userName;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public UUID getCycleId() {
        return cycleId;
    }

    public String getCycleName() {
        return cycleName;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
