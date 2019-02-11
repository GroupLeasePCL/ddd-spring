package com.example.lunchtimeboot.infrastructure.client.restaurant.entity;

import com.example.lunchtimeboot.infrastructure.client.restaurant.value.Location;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

public class Restaurant {

    private UUID id;
    private String name;
    private Location location;

    public Restaurant(UUID id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static Restaurant fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        UUID id = UUID.fromString(jsonNode.get("id").asText());
        String name = jsonNode.get("name").asText();

        JsonNode locationNode = jsonNode.get("location");
        Double latitude = locationNode.get("latitude").asDouble();
        Double longitude = locationNode.get("longitude").asDouble();
        Location location = new Location(latitude, longitude);

        Restaurant restaurant = new Restaurant(id, name, location);

        return restaurant;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}