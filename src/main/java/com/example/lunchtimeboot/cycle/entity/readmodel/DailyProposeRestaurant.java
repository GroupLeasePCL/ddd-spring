package com.example.lunchtimeboot.cycle.entity.readmodel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class DailyProposeRestaurant {

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private String restaurantName;

    public DailyProposeRestaurant() {
    }

    public DailyProposeRestaurant(UUID restaurantId, String restaurantName) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
