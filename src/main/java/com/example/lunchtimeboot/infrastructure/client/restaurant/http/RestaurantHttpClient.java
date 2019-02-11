package com.example.lunchtimeboot.infrastructure.client.restaurant.http;

import com.example.lunchtimeboot.infrastructure.client.restaurant.entity.Restaurant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

@Service
public class RestaurantHttpClient {
    @Value("${lunchtime.http-client.restaurant.api.base-url}")
    private String baseUrl;

    public Restaurant getRestaurant(UUID restaurantId) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        String url = baseUrl + "/restaurants/" + restaurantId.toString();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        Restaurant restaurant = Restaurant.fromJson(response.getBody());

        return restaurant;
    }
}
