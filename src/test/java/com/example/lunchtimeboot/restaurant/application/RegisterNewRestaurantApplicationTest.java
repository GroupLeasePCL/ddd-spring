package com.example.lunchtimeboot.restaurant.application;

import com.example.lunchtimeboot.restaurant.entity.Restaurant;
import com.example.lunchtimeboot.restaurant.event.RestaurantRegisteredEvent;
import com.example.lunchtimeboot.restaurant.factory.RestaurantFactory;
import com.example.lunchtimeboot.restaurant.repository.RestaurantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterNewRestaurantApplicationTest {
    @Autowired
    private EntityManager em;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @Captor
    protected ArgumentCaptor<Object> publishEventCaptor;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantFactory restaurantFactory;

    private RegisterNewRestaurantApplication registerNewRestaurantApplication;

    @Before
    public void setUp() {
        registerNewRestaurantApplication = new RegisterNewRestaurantApplication(applicationEventPublisher, restaurantFactory, restaurantRepository);
    }

    @Test
    public void registerNewRestaurant() {
        /* given */
        String name = "mana";

        /* when */
        Restaurant restaurant = registerNewRestaurantApplication.registerNewRestaurant(name, null);

        /* then */
        // assert result object
        assertThat(restaurant)
                .extracting(Restaurant::getName)
                .isEqualTo(name);

        // assert data persisted
        UUID id = restaurant.getId();
        Restaurant found = em.find(Restaurant.class, id);
        assertThat(found).isNotNull();
        assertThat(restaurant.getName()).isEqualTo(name);
        assertThat(found.getLocation()).isNull();

        // assert published event
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(publishEventCaptor.capture());
        List<Object> capturedEvents = publishEventCaptor.getAllValues();
        assertThat(capturedEvents).hasSize(1);
        assertThat(capturedEvents.get(0)).isInstanceOf(RestaurantRegisteredEvent.class);
    }
}
