package com.example.lunchtimeboot.user.application.endpoint;

import com.example.lunchtimeboot.user.application.FavRestaurantApplication;
import com.example.lunchtimeboot.user.application.RegisterUserApplication;
import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.repository.UserRepository;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegisterUserApplication registerUserApplication;

    @MockBean
    private FavRestaurantApplication favRestaurantApplication;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUpUser() {
        UUID id = UUID.fromString("11111111-1111-1111-1111-000000000000");
        String name = "tee.tan";
        String email = "tee.tan@email.com";
        String mobile = "0810000000";
        user = new User(id, name, email, mobile);
    }

    @Test
    public void registerNewUser() throws Exception {
        /* given */
        UUID id = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        String mobile = user.getMobile();

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        requestBody.put("email", email);
        requestBody.put("mobile", mobile);

        // mock
        User user = new User(id, name, email, mobile);
        given(registerUserApplication.registerNewUser(any(String.class), any(String.class), any(String.class)))
                .willReturn(user);

        /* when */
        ResultActions result = mvc.perform(post("/user/api/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.toString()));

        /* then */
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void getUserFound() throws Exception {
        /* given */
        UUID id = user.getId();

        // mock
        Optional<User> found = Optional.of(user);
        given(userRepository.findById(id)).willReturn(found);

        /* when */
        ResultActions result = mvc.perform(get("/user/api/users/" + id.toString())
                .contentType(MediaType.APPLICATION_JSON));

        /* then */
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(found.get().getName())));
    }

    @Test
    public void searchUser() throws Exception {
        /* given */
        String searchText = "tee";
        ArrayList<String> foundUserNames = new ArrayList<>(
                Arrays.asList("tee.tan", "me.tee", "mr.tee.tan")
        );

        // mock repository result
        ArrayList<User> restaurants = new ArrayList<>();
        foundUserNames.forEach(name -> restaurants.add(new User(UUID.randomUUID(), name, name + "@email.com", null)));
        given(userRepository.searchByName(searchText)).willReturn(restaurants);

        /* when */
        ResultActions result = mvc.perform(get("/user/api/users/search/" + searchText)
                .contentType(MediaType.APPLICATION_JSON));

        /* then */
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$..name", is(foundUserNames)));

    }

    @Test
    public void favRestaurant() throws Exception {
        /* given */
        String restaurantName = "Pizza Hut";
        UUID restaurantId = UUID.fromString("99999999-9999-9999-9999-000000000000");

        JSONObject requestBody = new JSONObject();
        requestBody.put("restaurantName", restaurantName);
        requestBody.put("restaurantId", restaurantId.toString());

        // mock
        user.fav(restaurantName, restaurantId);
        user.clearEvents();
        given(favRestaurantApplication.favRestaurant(any(UUID.class), any(String.class), any(UUID.class)))
                .willReturn(user);

        /* when */
        ResultActions result = mvc.perform(post("/user/api/users/" + user.getId().toString() + "/fav")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString())
        );

        /* then */
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.favouriteRestaurants.[:1].name", contains(restaurantName)))
                .andExpect(jsonPath("$.favouriteRestaurants.[:1].restaurantId", contains(restaurantId.toString())));
    }
}
