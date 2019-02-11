package com.example.lunchtimeboot.cycle.application;

import com.example.lunchtimeboot.cycle.entity.Cycle;
import com.example.lunchtimeboot.cycle.entity.CycleGuardException;
import com.example.lunchtimeboot.cycle.entity.CycleMember;
import com.example.lunchtimeboot.cycle.event.CycleJoinedEvent;
import com.example.lunchtimeboot.cycle.repository.CycleRepository;
import com.example.lunchtimeboot.infrastructure.client.user.http.MockServerFixture;
import com.example.lunchtimeboot.infrastructure.client.user.http.UserHttpClient;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.test.properties")

public class JoinCycleApplicationTest {

    private static final String mockServerUrl = "localhost";
    private static final int mockServerPort = 38080;

    private ClientAndServer mockServer;

    @Autowired
    private EntityManager em;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private UserHttpClient userHttpClient;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @Captor
    protected ArgumentCaptor<Object> publishEventCaptor;

    private JoinCycleApplication joinCycleApplication;

    private UUID userId;
    private String userName;
    private String userEmail;
    private String userMobile;

    private Cycle cycle;
    private CycleMember member;

    @Before
    public void startMockServer() {
        mockServer = startClientAndServer(mockServerPort);
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }

    @Before
    public void testUser() {
        userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        userName = "tee.tan";
        userEmail = "tee.tan@email.com";
        userMobile = "0811111111";
    }

    @Before
    public void setUp() {
        joinCycleApplication = new JoinCycleApplication(applicationEventPublisher, cycleRepository, userHttpClient);

        UUID memberId = UUID.randomUUID();
        String memberName = "johari.sri";
        UUID memberUserId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        member = new CycleMember(memberId, memberName, memberUserId);

        UUID cycleId = UUID.fromString("99999999-9999-9999-9999-000000000");
        String cycleName = "Luncheon Gangster";
        cycle = new Cycle(cycleId, cycleName, member);
        cycle.clearEvents();

        em.persist(cycle);
    }

    @Test
    @Transactional
    public void joinCycle() throws JSONException, CycleApplicationException, IOException, CycleGuardException {
        /* given */
        new MockServerFixture().mockServerGetUser(mockServerUrl, mockServerPort);

        /* when */
        Cycle cycle = joinCycleApplication.joinCycle(this.cycle.getId(), userId);

        /* then */
        // assert result object
        assertThat(cycle.getMembers()).hasSize(2);
        assertThat(cycle.getMembers().get(1))
                .extracting(CycleMember::getUserId)
                .isEqualTo(userId);
        assertThat(cycle.getMembers().get(1))
                .extracting(CycleMember::getName)
                .isEqualTo(userName);

        // assert data persisted
        UUID id = cycle.getId();
        Cycle found = em.find(Cycle.class, id);
        assertThat(found).isNotNull();
        assertThat(cycle.getMembers()).hasSize(2);
        assertThat(cycle.getMembers().get(1))
                .extracting(CycleMember::getName)
                .isEqualTo(userName);
        assertThat(cycle.getMembers().get(1))
                .extracting(CycleMember::getUserId)
                .isEqualTo(userId);

        // assert published event
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(publishEventCaptor.capture());
        List<Object> capturedDispatchedEvents = publishEventCaptor.getAllValues();
        assertThat(capturedDispatchedEvents).hasSize(1);
        assertThat(capturedDispatchedEvents.get(0)).isInstanceOf(CycleJoinedEvent.class);

    }
}
