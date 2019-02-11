package com.example.lunchtimeboot.restaurant.application.endpoint;

import com.example.lunchtimeboot.restaurant.application.MoveRestaurantApplication;
import com.example.lunchtimeboot.restaurant.application.RegisterNewRestaurantApplication;
import com.example.lunchtimeboot.restaurant.entity.Restaurant;
import com.example.lunchtimeboot.restaurant.repository.RestaurantRepository;
import com.example.lunchtimeboot.restaurant.value.Location;
import org.json.JSONObject;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegisterNewRestaurantApplication registerNewRestaurantApplication;

    @MockBean
    private MoveRestaurantApplication moveRestaurantApplication;

    @MockBean
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant;

    @Before
    public void setUpRestaurant() {
        UUID id = UUID.fromString("11111111-1111-1111-1111-000000000000");
        String name = "Pizza Hut";
        Location location = new Location(100.0000, 100.0000);
        restaurant = new Restaurant(id, name, location);
    }

    @Test
    public void registerNewRestaurant() throws Exception {
        /* given */
        UUID id = restaurant.getId();
        String name = restaurant.getName();
        Location location = restaurant.getLocation();

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        JSONObject locationJson = new JSONObject();
        locationJson.put("latitude", location.getLatitude());
        locationJson.put("longitude", location.getLongitude());
        requestBody.put("location", locationJson);

        // mock
        Restaurant restaurant = new Restaurant(id, name, location);
        given(registerNewRestaurantApplication.registerNewRestaurant(any(String.class), any(Location.class))).willReturn(restaurant);

        /* when */
        ResultActions result = mvc.perform(post("/restaurant/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.toString()));

        /* then */
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(restaurant.getName())));
    }

    @Test
    public void getRestaurantFound() throws Exception {
        /* given */
        UUID id = restaurant.getId();

        // mock
        Optional<Restaurant> found = Optional.of(restaurant);
        given(restaurantRepository.findById(id)).willReturn(found);

        /* when */
        ResultActions result = mvc.perform(get("/restaurant/api/restaurants/" + id.toString())
                .contentType(MediaType.APPLICATION_JSON));

        /* then */
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(found.get().getName())));
    }

    @Test
    public void getRestaurantNotFound() throws Exception {
        /* given */
        UUID id = restaurant.getId();

        // mock
        Optional<Restaurant> notFound = Optional.empty();
        given(restaurantRepository.findById(id)).willReturn(notFound);

        /* when */
        ResultActions result = mvc.perform(get("/restaurant/api/restaurants/" + id.toString())
                .contentType(MediaType.APPLICATION_JSON));

        /* then */
        result.andExpect(status().isNotFound());
    }

    @Test
    public void moveRestaurant() throws Exception {
        /* check */
        assertThat(this.moveRestaurantApplication).isNotNull();

        /* given */
        UUID id = restaurant.getId();
        String name = restaurant.getName();
        Location location = restaurant.getLocation();

        // mock
        Restaurant restaurant = new Restaurant(id, name, location);
//        given(moveRestaurantApplication.moveRestaurant(same(id), same(location))).willReturn(restaurant);
        given(moveRestaurantApplication.moveRestaurant(any(UUID.class), any(Location.class))).willReturn(restaurant);

        JSONObject requestBody = new JSONObject();
        requestBody.put("latitude", location.getLatitude());
        requestBody.put("longitude", location.getLongitude());

        /* when */
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/restaurant/api/restaurants/" + id.toString() + "/location")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody.toString());
        ResultActions result = mvc.perform(builder);

        /* then */
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location.latitude", is(location.getLatitude())))
                .andExpect(jsonPath("$.location.longitude", is(location.getLongitude())));
    }

    @Test
    public void searchRestaurantFound() throws Exception {
        /* given */
        String searchText = "res";
        ArrayList<String> foundRestaurantNames = new ArrayList<>(
                Arrays.asList("Restaurant A", "Restaurant B", "Restaurant C")
        );

        // mock repository result
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        foundRestaurantNames.forEach(name -> restaurants.add(new Restaurant(UUID.randomUUID(), name, null)));
        given(restaurantRepository.searchByName(searchText)).willReturn(restaurants);

        /* when */
        ResultActions result = mvc.perform(get("/restaurant/api/restaurants/search/" + searchText)
                .contentType(MediaType.APPLICATION_JSON));

        /* then */
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$..name", is(foundRestaurantNames)));
    }
}
