package com.example.lunchtimeboot.user.application.endpoint;

import com.example.lunchtimeboot.infrastructure.client.restaurant.value.Location;
import com.example.lunchtimeboot.infrastructure.client.restaurant.event.RestaurantLocationMovedEvent;
import com.example.lunchtimeboot.user.application.FavouriteRestaurantNotifierApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMessageQueueTest {

//    @Autowired
//    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FavouriteRestaurantNotifierApplication favouriteRestaurantNotifierApplication;

    private UserMessageQueue userMessageQueue;

    @Before
    public void setUp() {
        this.userMessageQueue = new UserMessageQueue(objectMapper, favouriteRestaurantNotifierApplication);
    }

    @Test
    public void messagesRestaurantLocationMovedEvent() throws IOException {
        /* given */
        UUID restaurantId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String restaurantName = "55 Baht";
        Location restaurantLocation = new Location(123.000, 456.000);
        RestaurantLocationMovedEvent restaurantMovedEvent = new RestaurantLocationMovedEvent(restaurantId, restaurantName, restaurantLocation);

        /* when */
        // @todo: this is better with functional/integration test
//        ObjectMapper objectMapper = new ObjectMapper();
//        String eventJson = objectMapper.writeValueAsString(restaurantMovedEvent);
//        jmsTemplate.convertAndSend(RestaurantLocationMovedEvent.PRODUCER_DESTINATION, eventJson);
//        jmsTemplate.setReceiveTimeout(10000);

        /* when */
        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(restaurantMovedEvent);
        userMessageQueue.sendFavouriteRestaurantMoved(eventJson);

        /* then */
        Mockito.verify(favouriteRestaurantNotifierApplication, Mockito.times(1))
                .sendRestaurantMovedNotification(restaurantId, restaurantName);
    }
}
