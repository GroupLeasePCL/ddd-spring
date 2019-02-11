package com.example.lunchtimeboot.cycle.factory;

import com.example.lunchtimeboot.cycle.entity.Cycle;
import com.example.lunchtimeboot.cycle.entity.CycleMember;
import com.example.lunchtimeboot.infrastructure.client.user.entity.User;
import com.example.lunchtimeboot.infrastructure.uuid.UuidGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CycleFactoryTest {
    @Autowired
    private CycleFactory cycleFactory;

    @MockBean
    private UuidGenerator uuidGenerator;

    private UUID cycleId = UUID.fromString("11111111-1111-1111-1111-000000000000");
    private UUID cycleMemberId = UUID.fromString("99999999-9999-9999-9999-000000000000");

    @Before
    public void setUp() {
        Mockito.when(uuidGenerator.createUuid4())
                .thenReturn(cycleId)
                .thenReturn(cycleMemberId);
    }

    @Test
    public void create() {
        /* given */
        String cycleName = "Luncheon Gangster";

        UUID userId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        String userName = "tee.tan";
        String userEmail = "tee.tan@email.com";
        String userMobile = "0810000000";
        User user = new User(userId, userName, userEmail, userMobile);

        /* when */
        Cycle cycle = cycleFactory.create(cycleName, user);

        /* then */
        assertThat(cycle.getName()).isEqualTo(cycleName);
        assertThat(cycle.getMembers()).hasSize(1);
        assertThat(cycle.getMembers().get(0))
                .extracting(CycleMember::getName)
                .isEqualTo(userName);
        assertThat(cycle.getMembers().get(0))
                .extracting(CycleMember::getUserId)
                .isEqualTo(userId);
    }
}