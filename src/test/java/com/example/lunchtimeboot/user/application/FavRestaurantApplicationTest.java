package com.example.lunchtimeboot.user.application;

import com.example.lunchtimeboot.infrastructure.activemq.Producer;
import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.entity.UserFavouriteRestaurant;
import com.example.lunchtimeboot.user.entity.UserGuardException;
import com.example.lunchtimeboot.user.event.UserFavedEvent;
import com.example.lunchtimeboot.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class FavRestaurantApplicationTest {
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
    private UserRepository userRepository;

    private FavRestaurantApplication favRestaurantApplication;

    private UUID userId = UUID.fromString("11111111-1111-1111-1111-000000000000");
    private String userName = "tee.tan";
    private String userEmail = "tee.tan@email.com";
    private String userMobile = "0811111111";

    @Before
    public void setUp() {
        favRestaurantApplication = new FavRestaurantApplication(applicationEventPublisher, messageQueueProducer, userRepository);

        User user = new User(userId, userName, userEmail, userMobile);
        user.clearEvents();
        em.persist(user);
        em.flush();
    }

    @Test
    @Transactional
    public void favRestaurant() throws JsonProcessingException, UserApplicationException, UserGuardException {
        /* given */
        String favRestaurantName = "Bon Mache";
        UUID favRestaurantId = UUID.fromString("99999999-9999-9999-9999-000000000000");

        /* when */
        User user = favRestaurantApplication.favRestaurant(userId, favRestaurantName, favRestaurantId);

        /* then */
        // assert result object
        assertThat(user.getFavouriteRestaurants()).hasSize(1);
        assertThat(user.getFavouriteRestaurants().get(0))
                .extracting(UserFavouriteRestaurant::getRestaurantId)
                .isEqualTo(favRestaurantId);
        assertThat(user.getFavouriteRestaurants().get(0))
                .extracting(UserFavouriteRestaurant::getName)
                .isEqualTo(favRestaurantName);

        // assert data persisted
        UUID id = user.getId();
        User found = em.find(User.class, id);
        assertThat(found.getFavouriteRestaurants()).hasSize(1);
        assertThat(found.getFavouriteRestaurants().get(0))
                .extracting(UserFavouriteRestaurant::getRestaurantId)
                .isEqualTo(favRestaurantId);
        assertThat(found.getFavouriteRestaurants().get(0))
                .extracting(UserFavouriteRestaurant::getName)
                .isEqualTo(favRestaurantName);

        // assert published event
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(publishEventCaptor.capture());
        List<Object> capturedDispatchedEvents = publishEventCaptor.getAllValues();
        assertThat(capturedDispatchedEvents).hasSize(1);
        assertThat(capturedDispatchedEvents.get(0)).isInstanceOf(UserFavedEvent.class);

        // assert mq produced event
        Mockito.verify(messageQueueProducer, Mockito.times(2))
                .publish(producerEventNameCaptor.capture(), producerEventCaptor.capture());

        // mq: destination name
        List<String> eventNames = producerEventNameCaptor.getAllValues();
        assertThat(eventNames).hasSize(2);
        assertThat(eventNames.get(0))
                .isEqualTo(Constant.PRODUCER_DESTINATION_USER);
        assertThat(eventNames.get(1))
                .isEqualTo(Constant.PRODUCER_DESTINATION_USER_FAVED_RESTAURANT);

        // mq: event json(s)
        List<String> capturedMqProducedEvents = producerEventCaptor.getAllValues();
        assertThat(capturedMqProducedEvents).hasSize(2);
        assertThat(capturedMqProducedEvents.get(0))
                .isEqualTo(new ObjectMapper().writeValueAsString(found.getEvents().get(0)));
        assertThat(capturedMqProducedEvents.get(1))
                .isEqualTo(new ObjectMapper().writeValueAsString(found.getEvents().get(0)));
    }
}