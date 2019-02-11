package com.example.lunchtimeboot.user.repository;

import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.entity.UserGuardException;
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
public class UserRepositoryImplTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findById() {
        /* given */
        UUID id = UUID.randomUUID();
        User user = new User(id, "tee.tan", "tee.tan@email.com", "0810000000");
        em.persistAndFlush(user);

        /* when */
        Optional<User> found = userRepository.findById(id);

        /* then */
        assertThat(found.isPresent()).isTrue();
        found.ifPresent(res -> assertThat(res.getName()).isEqualTo(user.getName()));
    }

    @Test
    public void searchByNameContainWord() {
        /* given */
        String searchString = "tee";

        ArrayList<String> foundNames = new ArrayList<>(
                Arrays.asList("tee.tan", "tan.tee", "mr.tee.tan")
        );

        ArrayList<String> notFoundNames = new ArrayList<>(
                Arrays.asList("not.found", "another.not.found")
        );

        Consumer<String> persistUser = name -> {
            System.out.println(name + "@mail.com");
            User restaurant = new User(UUID.randomUUID(), name, name + "@mail.com", null);
            em.persist(restaurant);
        };

        foundNames.forEach(persistUser);
        notFoundNames.forEach(persistUser);

        em.flush();

        /* when */
        List<User> users = userRepository.searchByName(searchString);

        /* then */
        ArrayList<String> savedNames = new ArrayList<>();
        foundNames.forEach(n -> savedNames.add(n.toLowerCase()));
        assertThat(users)
                .extracting(User::getName)
                .containsExactlyInAnyOrderElementsOf(savedNames);
    }

    @Test
    public void searchByNameResultLimit() {
        /* given */
        String searchString = "tee";
        int numAll = 200;
        int numAllMatch = 150;
        int numNotFound = numAll - numAllMatch;
        int limit = Constants.MAX_PAGE_SIZE;

        ArrayList<String> foundNames = new ArrayList<>();
        for (int i = 0; i < numAllMatch; i++) {
            String name = searchString + i;
            String email = name + "@email.com";
            User user = new User(UUID.randomUUID(), name, email, null);
            em.persist(user);
            foundNames.add(name);
        }

        for (int i = 0; i < numNotFound; i++) {
            UUID id = UUID.randomUUID();
            User user = new User(id, id.toString(), id.toString() + "@email.com", null);
            em.persist(user);
        }

        em.flush();

        /* when */
        List<User> users = userRepository.searchByName(searchString);

        /* then */
        assertThat(users)
                .hasSize(limit)
                .extracting(User::getName)
                .isSubsetOf(foundNames);
    }

    @Test
    public void findByFavouriteRestaurant() throws UserGuardException {
        /* given */
        UUID userFavId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID restaurantUserFavId = UUID.fromString("99999999-9999-9999-9999-999999999999");
        UUID userFavAnotherId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID restaurantUserFavAnotherId = UUID.randomUUID();

        User userFav = new User(userFavId, "tee.tan", "tee.tan@email.com", "0810000000");
        userFav.fav("Bak Kut Teh", restaurantUserFavId);
        em.persist(userFav);

        User userFavAnother = new User(userFavAnotherId, "tina", "christina.d@email.com", "0820000000");
        userFavAnother.fav("Gold Medal", restaurantUserFavAnotherId);
        em.persist(userFavAnother);

        em.flush();

        /* when */
        List<User> users = userRepository.findByFavouriteRestaurantId(restaurantUserFavId);

        /* then */
        assertThat(users)
                .hasSize(1)
                .extracting(User::getId)
                .containsOnly(userFavId);
    }
}
