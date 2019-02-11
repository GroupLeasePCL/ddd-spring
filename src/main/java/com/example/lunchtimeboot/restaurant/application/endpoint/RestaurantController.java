package com.example.lunchtimeboot.restaurant.application.endpoint;

import com.example.lunchtimeboot.restaurant.application.MoveRestaurantApplication;
import com.example.lunchtimeboot.restaurant.application.RegisterNewRestaurantApplication;
import com.example.lunchtimeboot.restaurant.application.RestaurantApplicationException;
import com.example.lunchtimeboot.restaurant.entity.Restaurant;
import com.example.lunchtimeboot.restaurant.entity.RestaurantGuardException;
import com.example.lunchtimeboot.restaurant.repository.RestaurantRepository;
import com.example.lunchtimeboot.restaurant.value.Location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/restaurant/api")
public class RestaurantController {

    private RegisterNewRestaurantApplication registerNewRestaurantApplication;
    private MoveRestaurantApplication moveRestaurantApplication;
    private RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantController(RegisterNewRestaurantApplication registerNewRestaurantApplication, MoveRestaurantApplication moveRestaurantApplication, RestaurantRepository restaurantRepository) {
        this.registerNewRestaurantApplication = registerNewRestaurantApplication;
        this.moveRestaurantApplication = moveRestaurantApplication;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping("/restaurants")
    public ResponseEntity registerNewRestaurant(@RequestBody Restaurant body) {
        String name = body.getName();
        Location location = body.getLocation();

        Restaurant restaurant = registerNewRestaurantApplication.registerNewRestaurant(name, location);

        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }

    @PatchMapping("/restaurants/{id}/location")
    public ResponseEntity moveRestaurant(@PathVariable UUID id, @RequestBody Location location) throws RestaurantGuardException, RestaurantApplicationException, JsonProcessingException {
        Restaurant restaurant = moveRestaurantApplication.moveRestaurant(id, location);

        return ResponseEntity.status(HttpStatus.OK).body(restaurant);
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity getRestaurant(@PathVariable UUID id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);

        if (!restaurant.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(restaurant);
    }

    @GetMapping("/restaurants/search/{search}")
    public ResponseEntity searchRestaurant(@PathVariable String search) {
        List restaurants = restaurantRepository.searchByName(search);

        if (restaurants.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(restaurants);
    }
}
