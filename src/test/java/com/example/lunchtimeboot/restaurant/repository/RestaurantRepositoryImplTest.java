package com.example.lunchtimeboot.restaurant.repository;

import com.example.lunchtimeboot.restaurant.entity.Restaurant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RestaurantRepositoryImplTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    public void findById() {
        /* given */
        UUID id = UUID.randomUUID();
        Restaurant restaurant = new Restaurant(id, "Bon Mache", null);
        em.persistAndFlush(restaurant);

        /* when */
        Optional<Restaurant> found = restaurantRepository.findById(id);

        /* then */
        assertThat(found.isPresent()).isTrue();
        found.ifPresent(res -> assertThat(res.getName()).isEqualTo(restaurant.getName()));
    }

    @Test
    public void searchByNameContainWord() {
        /* given */
        String searchString = "Aloha";

        ArrayList<String> foundNames = new ArrayList<>(
                Arrays.asList("Aloha A", "Tribe Aloha", "Uhu Alohahaha!")
        );

        ArrayList<String> notFoundNames = new ArrayList<>(
                Arrays.asList("Not Found", "Okay I can make it two thanks IDE")
        );

        Consumer<String> persistRestaurant = name -> {
                Restaurant restaurant = new Restaurant(UUID.randomUUID(), name, null);
                em.persist(restaurant);
        };

        foundNames.forEach(persistRestaurant);
        notFoundNames.forEach(persistRestaurant);

        em.flush();

        /* when */
        List<Restaurant> restaurants = restaurantRepository.searchByName(searchString);

        /* then */
        assertThat(restaurants)
                .extracting(Restaurant::getName)
                .containsExactlyInAnyOrderElementsOf(foundNames);
    }

    @Test
    public void searchByNameResultLimit() {
        /* given */
        String searchString = "Aloha";
        int numAll = 200;
        int numAllMatch = 150;
        int numNotFound = numAll - numAllMatch;
        int limit = Constants.MAX_PAGE_SIZE;

        ArrayList<String> foundNames = new ArrayList<>();
        for (int i = 0; i < numAllMatch; i++) {
            String name = searchString + ' ' + i;
            Restaurant restaurant = new Restaurant(UUID.randomUUID(), name, null);
            em.persist(restaurant);
            foundNames.add(name);
            System.out.println(name);
        }

        for (int i = 0; i < numNotFound; i++) {
            UUID id = UUID.randomUUID();
            Restaurant restaurant = new Restaurant(id, id.toString(), null);
            em.persist(restaurant);
        }

        em.flush();

        /* when */
        List<Restaurant> restaurants = restaurantRepository.searchByName(searchString);

        /* then */
        assertThat(restaurants)
                .hasSize(limit)
                .extracting(Restaurant::getName)
                .isSubsetOf(foundNames);
    }
}
