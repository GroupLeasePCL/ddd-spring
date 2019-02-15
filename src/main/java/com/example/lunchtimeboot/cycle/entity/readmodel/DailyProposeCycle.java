package com.example.lunchtimeboot.cycle.entity.readmodel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class DailyProposeCycle {

    @Column(nullable = false)
    private UUID cycleId;

    @Column(nullable = false)
    private String cycleName;

    public DailyProposeCycle() {
    }

    public DailyProposeCycle(UUID cycleId, String cycleName) {
        this.cycleId = cycleId;
        this.cycleName = cycleName;
    }

    public UUID getCycleId() {
        return cycleId;
    }

    public void setCycleId(UUID cycleId) {
        this.cycleId = cycleId;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }
}
