package com.example.lunchtimeboot.cycle.entity;

import com.example.lunchtimeboot.cycle.event.ProposeMadeEvent;
import com.example.lunchtimeboot.infrastructure.ddd.BaseAggregate;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Propose extends BaseAggregate {

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private LocalDate forDate;

    public Propose() {
    }

    public Propose(UUID id, UUID userId, UUID restaurantId, LocalDate forDate) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.forDate = forDate;

        addEvent(new ProposeMadeEvent(this, id, userId, restaurantId, forDate));
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public LocalDate getForDate() {
        return forDate;
    }

    public void setForDate(LocalDate forDate) {
        this.forDate = forDate;
    }
}
