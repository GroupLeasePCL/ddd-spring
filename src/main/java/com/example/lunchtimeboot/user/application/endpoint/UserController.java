package com.example.lunchtimeboot.user.application.endpoint;

import com.example.lunchtimeboot.user.application.FavRestaurantApplication;
import com.example.lunchtimeboot.user.application.RegisterUserApplication;
import com.example.lunchtimeboot.user.application.UserApplicationException;
import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.entity.UserGuardException;
import com.example.lunchtimeboot.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user/api")
public class UserController {

    private RegisterUserApplication registerUserApplication;
    private FavRestaurantApplication favRestaurantApplication;
    private UserRepository userRepository;

    @Autowired
    public UserController(RegisterUserApplication registerUserApplication, UserRepository userRepository, FavRestaurantApplication favRestaurantApplication) {
        this.registerUserApplication = registerUserApplication;
        this.favRestaurantApplication = favRestaurantApplication;
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity registerNewUser(@RequestBody User body) {
        String name = body.getName();
        String email = body.getEmail();
        String mobile = body.getMobile();

        User user = registerUserApplication.registerNewUser(name, email, mobile);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable UUID id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/search/{search}")
    public ResponseEntity searchUser(@PathVariable String search) {
        List<User> users = userRepository.searchByName(search);

        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(users);
    }

    @PostMapping("/users/{id}/fav")
    public ResponseEntity favRestaurant(@PathVariable UUID id, @RequestBody String jsonBody) throws IOException, UserApplicationException, UserGuardException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonBody);

        String restaurantName = jsonNode.get("restaurantName").asText();
        UUID restaurantId = UUID.fromString(jsonNode.get("restaurantId").asText());

        User user = favRestaurantApplication.favRestaurant(id, restaurantName, restaurantId);

        return ResponseEntity.ok(user);
    }
}
