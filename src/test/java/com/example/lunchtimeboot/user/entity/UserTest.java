package com.example.lunchtimeboot.user.entity;

import com.example.lunchtimeboot.infrastructure.ddd.Event;
import com.example.lunchtimeboot.user.event.UserFavedEvent;
import com.example.lunchtimeboot.user.event.UserRegisteredEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    private User user;

    @Before
    public void setUp() {
        UUID id = UUID.fromString("11111111-1111-1111-1111-000000000000");
        String name = "tee.tan";
        String email = "tee.tan@email.com";
        String mobile = "0810000000";
        user = new User(id, name, email, mobile);
        user.clearEvents();
    }

    @Test
    public void create() {
        /* given */
        UUID id = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        String mobile = user.getMobile();

        /* when */
        user = new User(id, name, email, mobile);

        /* then */
        assertThat(user.getEvents()).hasSize(1);
        assertThat(user.getEvents().get(0))
                .isInstanceOf(UserRegisteredEvent.class)
                .isEqualToComparingFieldByField(
                        new UserRegisteredEvent(id, name, email, mobile)
                );
    }

    @Test
    public void fav() throws UserGuardException {
        /* given */
        UUID restaurantId = UUID.fromString("11111111-1111-1111-1111-000000000000");
        String restaurantName = "Gold Medal";

        /* when */
        user.fav(restaurantName, restaurantId);

        /* then */
        // assert change
        List<UserFavouriteRestaurant> restaurants = user.getFavouriteRestaurants();
        assertThat(restaurants.size())
                .isEqualTo(1);
        assertThat(restaurants.get(0).getRestaurantId())
                .isEqualTo(restaurantId);

        // assert event
        List<Event> events = user.getEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
                .isInstanceOf(UserFavedEvent.class)
                .extracting("userFavouriteRestaurantId")
                .containsOnly(restaurantId);
    }

    @Test
    public void favAlreadyFavedRestaurant() {
        /* given */
        UUID restaurantId = UUID.fromString("11111111-1111-1111-1111-000000000000");
        String restaurantName = "Gold Medal";

        /* when */
        try {
            user.fav(restaurantName, restaurantId);
            user.fav(restaurantName, restaurantId);
            fail("Exception expected: " + UserGuardException.class);
        } catch (UserGuardException $e) {
            /* then */
            assertThat($e).isInstanceOf(UserGuardException.class);
            assertThat($e.getCode()).isEqualTo(UserGuardException.FAV_ALREADY_FAVED_RESTAURANT);
            assertThat($e.getMessage()).contains(user.getId().toString());
            assertThat($e.getMessage()).contains(user.getName());
            assertThat($e.getMessage()).contains(restaurantId.toString());
        }
    }
}
