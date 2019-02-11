package com.example.lunchtimeboot.restaurant.application;

import com.example.lunchtimeboot.infrastructure.activemq.Producer;
import com.example.lunchtimeboot.restaurant.entity.Restaurant;
import com.example.lunchtimeboot.restaurant.entity.RestaurantGuardException;
import com.example.lunchtimeboot.restaurant.event.Constant;
import com.example.lunchtimeboot.restaurant.event.RestaurantLocationMovedEvent;
import com.example.lunchtimeboot.restaurant.repository.RestaurantRepository;
import com.example.lunchtimeboot.restaurant.value.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoveRestaurantApplicationTest {
    @Autowired
    private EntityManager em;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @Captor
    protected ArgumentCaptor<Object> publishEventCaptor;

    @MockBean
    private Producer messageQueueProducer;

    @Captor
    protected ArgumentCaptor<String> producerEventCaptor;

    @Captor
    protected ArgumentCaptor<String> producerEventNameCaptor;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private MoveRestaurantApplication moveRestaurantApplication;

    private UUID uuid = UUID.fromString("11111111-1111-1111-1111-000000000000");
    private Location location = new Location(13.7563, 100.5018);

    @Before
    public void setUp() {
        moveRestaurantApplication = new MoveRestaurantApplication(applicationEventPublisher, messageQueueProducer, restaurantRepository);

        Restaurant restaurant = new Restaurant(uuid, "Nai Sai", location);
        restaurant.clearEvents();
        em.persist(restaurant);
        em.flush();
    }

    @Test
    @Transactional // don't know why but here >> https://stackoverflow.com/a/32552558
    public void moveRestaurant() throws Exception {
        /* given */
        Location newLocation = new Location(13.7000, 100.5000);

        /* when */
        Restaurant restaurant = moveRestaurantApplication.moveRestaurant(uuid, newLocation);

        /* then */
        // assert result object
        assertThat(restaurant)
                .extracting(Restaurant::getLocation)
                .isEqualTo(newLocation);

        // assert data persisted
        UUID id = restaurant.getId();
        Restaurant found = em.find(Restaurant.class, id);
        assertThat(found.getLocation()).isEqualTo(newLocation);

        // assert published event
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(publishEventCaptor.capture());
        List<Object> capturedDispatchedEvents = publishEventCaptor.getAllValues();
        assertThat(capturedDispatchedEvents).hasSize(1);
        assertThat(capturedDispatchedEvents.get(0)).isInstanceOf(RestaurantLocationMovedEvent.class);

        // assert mq produced event
        Mockito.verify(messageQueueProducer, Mockito.times(2))
                .publish(producerEventNameCaptor.capture(), producerEventCaptor.capture());

        // mq: destination names
        List<String> eventNames = producerEventNameCaptor.getAllValues();
        assertThat(eventNames).hasSize(2);
        assertThat(eventNames.get(0))
                .isEqualTo(Constant.PRODUCER_DESTINATION_RESTAURANT);
        assertThat(eventNames.get(1))
                .isEqualTo(Constant.PRODUCER_DESTINATION_RESTAURANT_MOVED);

        // mq: event json(s)
        List<String> capturedMqProducedEvents = producerEventCaptor.getAllValues();
        assertThat(capturedMqProducedEvents).hasSize(2);
    }

    @Test
    @Transactional
    public void moveRestaurantSameLocation() {
        /* given */

        try {
            /* when */
            Restaurant restaurant = moveRestaurantApplication.moveRestaurant(uuid, location);
            fail("Expect Exception: " + RestaurantGuardException.class);
        } catch (Exception $e) {
            /* then */
            // assert exception
            assertThat($e).isInstanceOf(RestaurantGuardException.class);

            // assert data is not changed
            Restaurant found = em.find(Restaurant.class, uuid);
            assertThat(found).isInstanceOf(Restaurant.class);
            assertThat(found.getLocation()).isEqualTo(location);

            // assert event is not published
            Mockito.verify(applicationEventPublisher, Mockito.never())
                    .publishEvent(publishEventCaptor.capture());
        }
    }

    @Test
    @Transactional
    public void moveNonExistingRestaurant() throws Exception {
        /* given */
        UUID nonExistId = UUID.fromString("11111111-1111-1111-1111-999900000000");
        Location newLocation = new Location(13.7000, 100.5000);

        try {
            /* when */
            Restaurant restaurant = moveRestaurantApplication.moveRestaurant(nonExistId, newLocation);
            fail("Expect Exception: " + RestaurantGuardException.class);
        } catch (RestaurantApplicationException $e) {
            /* then */
            // assert exception
            assertThat($e).isInstanceOf(RestaurantApplicationException.class);
            assertThat($e.getCode()).isEqualTo(RestaurantApplicationException.RESTAURANT_NOT_FOUND);
            assertThat($e.getMessage()).contains(nonExistId.toString());

            // assert event is not published
            Mockito.verify(applicationEventPublisher, Mockito.never())
                    .publishEvent(publishEventCaptor.capture());
        }
    }
}
