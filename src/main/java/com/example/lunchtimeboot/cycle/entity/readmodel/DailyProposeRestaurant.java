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
}
