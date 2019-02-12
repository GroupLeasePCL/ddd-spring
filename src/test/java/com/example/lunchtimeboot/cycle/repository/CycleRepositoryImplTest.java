package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.Cycle;
import com.example.lunchtimeboot.cycle.entity.CycleGuardException;
import com.example.lunchtimeboot.cycle.entity.CycleMember;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CycleRepositoryImplTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CycleRepository cycleRepository;

    @Test
    public void findByMemberUserId() throws CycleGuardException {
        /* given */
        UUID firstUserId = UUID.randomUUID();
        String firstUserName = "tee.tan";

        UUID secondUserId = UUID.randomUUID();
        String secondUserName = "johari.sri";

        String firstCycleName = "Luncheon Gangster";
        String secondCycleName = "Diners Gangster";

        Cycle lunchCycle = new Cycle(
                UUID.randomUUID(),
                firstCycleName,
                (new CycleMember(UUID.randomUUID(), firstUserName, firstUserId))
        );
        lunchCycle.join(secondUserName, secondUserId);
        lunchCycle.clearEvents();
        em.persist(lunchCycle);

        Cycle dinerCycle = new Cycle(
                UUID.randomUUID(),
                secondCycleName,
                (new CycleMember(UUID.randomUUID(), secondUserName, secondUserId))
        );
        dinerCycle.clearEvents();
        em.persist(dinerCycle);

        em.flush();

        /* when */
        System.out.println(firstUserId.toString());
        List<Cycle> found = cycleRepository.findByMemberUserId(firstUserId);

        /* then */
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName())
                .isEqualTo(firstCycleName);
    }

}