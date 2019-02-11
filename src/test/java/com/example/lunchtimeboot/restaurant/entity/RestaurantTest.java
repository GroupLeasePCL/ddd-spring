package com.example.lunchtimeboot.restaurant.entity;

import com.example.lunchtimeboot.infrastructure.ddd.Event;
import com.example.lunchtimeboot.restaurant.event.RestaurantLocationMovedEvent;
import com.example.lunchtimeboot.restaurant.event.RestaurantRegisteredEvent;
import com.example.lunchtimeboot.restaurant.value.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class RestaurantTest {

    private Restaurant restaurant;

    @Before
    public void setUp() {
        UUID id = UUID.fromString("11111111-1111-1111-1111-000000000000");
        Location location = new Location(13.7563, 100.5018);
        restaurant = new Restaurant(id, "Bon Mache", location);
        restaurant.clearEvents();
    }

    @Test
    public void create() {
        /* given */
        UUID id = restaurant.getId();
        String name = restaurant.getName();
        Location location = restaurant.getLocation();

        /* when */
        Restaurant restaurant = new Restaurant(id, name, location);

        /* then */
        List<Event> events = restaurant.getEvents();
        assertThat(events)
                .hasSize(1);
        assertThat(events.get(0))
                .isInstanceOf(RestaurantRegisteredEvent.class)
                .isEqualToComparingFieldByField(new RestaurantRegisteredEvent(restaurant.getId(), restaurant.getName(), restaurant.getLocation()));
    }

    @Test
    public void moveLocation() throws RestaurantGuardException {
        /* given */
        Location newLocation = new Location(13.8000, 100.5000);

        /* when */
        restaurant.moveLocation(newLocation);

        /* then */
        // assert change
        assertThat(restaurant.getLocation())
                .isEqualTo(newLocation);

        // assert event
        List<Event> events = restaurant.getEvents();
        assertThat(events)
                .hasSize(1);
        assertThat(events.get(0))
                .isInstanceOf(RestaurantLocationMovedEvent.class)
                .extracting("restaurantLocation")
                .containsOnly(newLocation);
    }

    @Test
    public void moveToSameLocation() {
        /* given */
        Location sameLocation = restaurant.getLocation();

        /* when */
        try {
            restaurant.moveLocation(sameLocation);
            fail("Exception expected: " + RestaurantGuardException.class);
        } catch (RestaurantGuardException $e) {
            /* then */
            assertThat($e).isInstanceOf(RestaurantGuardException.class);
            assertThat($e.getCode()).isEqualTo(RestaurantGuardException.MOVE_SAME_LOCATION);
            assertThat($e.getMessage()).contains(sameLocation.getLatitude().toString());
            assertThat($e.getMessage()).contains(sameLocation.getLongitude().toString());
        }
    }
}
