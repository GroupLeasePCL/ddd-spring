package com.example.lunchtimeboot.infrastructure.client.user.http;

import com.example.lunchtimeboot.infrastructure.client.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserHttpClient {
    @Value("${lunchtime.http-client.user.api.base-url}")
    private String baseUrl;

    public User getUser(UUID userId) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        String url = baseUrl + "/users/" + userId.toString();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        User user = User.fromJson(response.getBody());

        return user;
    }
}
