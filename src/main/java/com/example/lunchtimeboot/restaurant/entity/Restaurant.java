package com.example.lunchtimeboot.restaurant.entity;

import com.example.lunchtimeboot.infrastructure.ddd.BaseAggregate;
import com.example.lunchtimeboot.restaurant.event.RestaurantLocationMovedEvent;
import com.example.lunchtimeboot.restaurant.event.RestaurantRegisteredEvent;
import com.example.lunchtimeboot.restaurant.value.Location;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Restaurant extends BaseAggregate {

    private static final Double LOCATION_THRESHOLD = .000001;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private Integer numFavourite;

    private Restaurant() {
    }

    public Restaurant(UUID id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.numFavourite = 0;

        addEvent(new RestaurantRegisteredEvent(id, name, location));
    }

    public Restaurant moveLocation(Location newLocation) throws RestaurantGuardException {
        this.guardMoveLocation(newLocation);

        this.location = newLocation;

        addEvent(new RestaurantLocationMovedEvent(id, name, newLocation));

        return this;
    }

    private void guardMoveLocation(Location newLocation) throws RestaurantGuardException {
        if (this.location != null
                && Math.abs(newLocation.getLatitude() - this.location.getLatitude()) < LOCATION_THRESHOLD
                && Math.abs(newLocation.getLongitude() - this.location.getLongitude()) < LOCATION_THRESHOLD
        ) {
            throw RestaurantGuardException.createMoveSameLocation(this, newLocation);
        }
    }

    public Restaurant replace(Restaurant restaurant, Location previousRestaurantNewLocation) {
        // nullify previous restaurant location if new location is not supplied
        return this;
    }

    public Restaurant discontinue() {
        return this;
    }

    @Override
    public String toString() {
        return String.format("Restaurant[%s]: name=%s", id, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getNumFavourite() {
        return numFavourite;
    }

    public void setNumFavourite(Integer numFavourite) {
        this.numFavourite = numFavourite;
    }
}
