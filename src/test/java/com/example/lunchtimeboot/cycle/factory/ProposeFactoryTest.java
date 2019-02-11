package com.example.lunchtimeboot.cycle.factory;

import com.example.lunchtimeboot.cycle.factory.ProposeFactory;
import com.example.lunchtimeboot.infrastructure.clock.SystemClock;
import com.example.lunchtimeboot.infrastructure.uuid.UuidGenerator;
import com.example.lunchtimeboot.cycle.entity.Propose;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProposeFactoryTest {
    @Autowired
    private ProposeFactory proposeFactory;

    @MockBean
    private UuidGenerator uuidGenerator;

    @MockBean
    private SystemClock systemClock;

    private UUID proposeId = UUID.fromString("11111111-1111-1111-1111-000000000000");
    private UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private UUID restaurantId = UUID.fromString("99999999-9999-9999-9999-999999999999");

    @Before
    public void setUp() {
        Mockito.when(uuidGenerator.createUuid4())
                .thenReturn(proposeId);

        Clock systemTime = Clock.fixed(Instant.parse("2000-01-01T09:00:00.00Z"), ZoneId.systemDefault());
        Mockito.when(systemClock.getClock())
                .thenReturn(systemTime);
    }

    @Test
    public void create() {
        /* given */
        LocalDate proposeDate = LocalDate.now(systemClock.getClock());

        /* when */
        Propose propose = proposeFactory.create(userId, restaurantId);

        /* then */
        assertThat(propose.getUserId()).isEqualTo(userId);
        assertThat(propose.getRestaurantId()).isEqualTo(restaurantId);
        assertThat(propose.getForDate()).isEqualTo(proposeDate);

    }
}