package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.Propose;
import com.example.lunchtimeboot.cycle.repository.ProposeRepository;
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
public class ProposeRepositoryImplTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProposeRepository proposeRepository;

    @Test
    public void findUserProposeForDate() {
        /* given */
        UUID userId = UUID.randomUUID();
        UUID firstDayProposeId = UUID.randomUUID();
        LocalDate firstDay = LocalDate.of(2000, 1, 1);
        UUID firstDayRestaurantId = UUID.randomUUID();

        UUID secondDayProposeId = UUID.randomUUID();
        LocalDate secondDay = LocalDate.of(2000, 1, 2);
        UUID secondDayRestaurantId = UUID.randomUUID();

        Propose first = new Propose(firstDayProposeId, userId, firstDayRestaurantId, firstDay);
        em.persist(first);
        Propose second = new Propose(secondDayProposeId, userId, secondDayRestaurantId, secondDay);
        em.persist(second);
        em.flush();

        /* when */
        Optional<Propose> found = proposeRepository.findUserProposeForDate(userId, secondDay);

        /* then */
        assertThat(found.isPresent()).isTrue();
        found.ifPresent(p -> assertThat(p.getRestaurantId()).isEqualTo(secondDayRestaurantId));
    }

}