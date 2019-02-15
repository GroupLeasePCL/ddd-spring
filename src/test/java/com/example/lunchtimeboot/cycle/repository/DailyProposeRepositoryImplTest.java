package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.readmodel.DailyPropose;
import com.example.lunchtimeboot.cycle.entity.readmodel.DailyProposeCycle;
import com.example.lunchtimeboot.cycle.entity.readmodel.DailyProposeRestaurant;
import com.example.lunchtimeboot.cycle.entity.readmodel.DailyProposeUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DailyProposeRepositoryImplTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private DailyProposeRepository dailyProposeRepository;

    @Test
    public void findCycleProposedRestaurantForDate() {
        /* given */
        UUID userId = UUID.randomUUID();
        String userName = "tee.tan";
        UUID cycleId = UUID.randomUUID();
        String cycleName = "Luncheon Gangster";
        UUID restaurantId = UUID.randomUUID();
        String restaurantName = "Bon Mache";
        LocalDate forDate = LocalDate.of(2000, 1, 1);

        DailyProposeRestaurant proposedRestaurant = new DailyProposeRestaurant(restaurantId, restaurantName);
        DailyProposeUser proposedUser = new DailyProposeUser(UUID.randomUUID(), userId, userName);
        DailyProposeCycle proposedCycle = new DailyProposeCycle(cycleId, cycleName);
        DailyPropose dailyPropose = new DailyPropose(proposedCycle, proposedRestaurant, proposedUser, forDate);

        em.persistAndFlush(dailyPropose);

        /* when */
        Optional<DailyPropose> result = dailyProposeRepository.findCycleProposedRestaurantForDate(cycleId, restaurantId, forDate);

        /* then */
        assertThat(result.isPresent()).isTrue();
        DailyPropose found = result.get();
        assertThat(found.getCycle()).isEqualTo(proposedCycle);
        assertThat(found.getRestaurant()).isEqualTo(proposedRestaurant);
        assertThat(found.getProposedUsers()).hasSize(1);
        assertThat(found.getProposedUsers().get(0))
                .extracting(DailyProposeUser::getUserId)
                .isEqualTo(userId);
        assertThat(found.getForDate()).isEqualTo(forDate);
    }
}
