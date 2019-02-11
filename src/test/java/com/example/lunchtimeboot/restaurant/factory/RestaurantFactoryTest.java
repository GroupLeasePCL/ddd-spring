package com.example.lunchtimeboot.restaurant.factory;

import com.example.lunchtimeboot.infrastructure.uuid.UuidGenerator;
import com.example.lunchtimeboot.restaurant.entity.Restaurant;
import com.example.lunchtimeboot.restaurant.value.Location;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestaurantFactory.class})
public class RestaurantFactoryTest {

    @Autowired
    private RestaurantFactory restaurantFactory;

    @MockBean
    private UuidGenerator uuidGenerator;

    private UUID uuid = UUID.fromString("11111111-1111-1111-1111-000000000000");

    @Before
    public void setUp() {
        Mockito.when(uuidGenerator.createUuid4())
                .thenReturn(uuid);
    }

    @Test
    public void createRestaurant() {
        String name = "2 Lovers";

        Restaurant restaurant = restaurantFactory.create(name, null);

        assertThat(restaurant.getId()).isEqualTo(uuid);
        assertThat(restaurant.getName()).isEqualTo(name);
        assertThat(restaurant.getLocation()).isNull();
    }

    @Test
    public void createRestaurantWithLocation() {
        String name = "2 Lovers";
        Location location = new Location(13.7563, 100.5018);

        Restaurant restaurant = restaurantFactory.create(name, location);

        assertThat(restaurant.getId()).isEqualTo(uuid);
        assertThat(restaurant.getName()).isEqualTo(name);
        assertThat(restaurant.getLocation()).isEqualTo(location);
    }
}
