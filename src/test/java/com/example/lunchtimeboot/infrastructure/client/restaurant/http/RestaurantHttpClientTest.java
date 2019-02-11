package com.example.lunchtimeboot.infrastructure.client.restaurant.http;

import com.example.lunchtimeboot.infrastructure.client.restaurant.entity.Restaurant;
import com.example.lunchtimeboot.infrastructure.client.restaurant.value.Location;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.test.properties")
public class RestaurantHttpClientTest {

    private static final String mockServerUrl = "localhost";
    private static final int mockServerPort = 38080;

    private ClientAndServer mockServer;

    @Autowired
    private RestaurantHttpClient restaurantHttpClient;

    private UUID restaurantId;
    private String restaurantName;
    private Location restaurantLocation;

    @Before
    public void setUp() {
        restaurantId = UUID.fromString("99999999-9999-9999-9999-000000000000");
        restaurantName = "Bon Mache";
        restaurantLocation = new Location(100.000001, 100.000001);
    }

    @Before
    public void startMockServer() {
        mockServer = startClientAndServer(mockServerPort);
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void getRestaurant() throws IOException, JSONException {
        new MockServerFixture().mockServerGetRestaurant(mockServerUrl, mockServerPort);
        Restaurant restaurant = restaurantHttpClient.getRestaurant(restaurantId);
        assertThat(restaurant.getId()).isEqualTo(restaurantId);
        assertThat(restaurant.getName()).isEqualTo(restaurantName);
        assertThat(restaurant.getLocation().getLatitude()).isEqualTo(restaurantLocation.getLatitude());
        assertThat(restaurant.getLocation().getLongitude()).isEqualTo(restaurantLocation.getLongitude());
    }
}
