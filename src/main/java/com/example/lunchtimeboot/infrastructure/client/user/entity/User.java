package com.example.lunchtimeboot.infrastructure.client.user.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String mobile;

    public User(UUID id, String name, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public static User fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        UUID id = UUID.fromString(jsonNode.get("id").asText());
        String name = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String mobile = jsonNode.get("mobile").asText();

        User user = new User(id, name, email, mobile);

        return user;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}
