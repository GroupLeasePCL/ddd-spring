package com.example.lunchtimeboot.user.application;

import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.entity.UserGuardException;
import com.example.lunchtimeboot.user.repository.UserRepository;
import com.example.lunchtimeboot.user.service.NotificationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FavouriteRestaurantNotifierApplicationTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private NotificationService notificationService;

    private FavouriteRestaurantNotifierApplication favouriteRestaurantNotifierApplication;

    @Before
    public void setUp() {
        favouriteRestaurantNotifierApplication = new FavouriteRestaurantNotifierApplication(userRepository, notificationService);
    }

    @Test
    @Transactional
    public void sendRestaurantNotificationMoved() throws UserGuardException {
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
        favouriteRestaurantNotifierApplication.sendRestaurantMovedNotification(restaurantUserFavId, "Bak Kut Teh");

        /* then */
        // assert notification sent
        Mockito.verify(notificationService, Mockito.times(1))
                .sendNotification("[tee.tan] Your favourite restaurant Bak Kut Teh is moved");
    }
}
