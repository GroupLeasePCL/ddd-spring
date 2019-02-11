package com.example.lunchtimeboot.cycle.application;

import com.example.lunchtimeboot.cycle.entity.Cycle;
import com.example.lunchtimeboot.cycle.entity.CycleMember;
import com.example.lunchtimeboot.cycle.factory.CycleFactory;
import com.example.lunchtimeboot.cycle.repository.CycleRepository;
import com.example.lunchtimeboot.infrastructure.client.user.http.MockServerFixture;
import com.example.lunchtimeboot.infrastructure.client.user.http.UserHttpClient;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.test.properties")
public class CreateCycleApplicationTest {

    private static final String mockServerUrl = "localhost";
    private static final int mockServerPort = 38080;

    private ClientAndServer mockServer;

    @Autowired
    private EntityManager em;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private CycleFactory cycleFactory;

    @Autowired
    private UserHttpClient userHttpClient;

    @Autowired
    private CreateCycleApplication createCycleApplication;

    private UUID userId;
    private String userName;
    private String userEmail;
    private String userMobile;

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

    @Test
    public void createCycle() throws IOException, JSONException {
        /* given */
        new MockServerFixture().mockServerGetUser(mockServerUrl, mockServerPort);
        String name = "Luncheon Gangster";

        /* when */
        Cycle cycle = createCycleApplication.createCycle(name, userId);

        /* then */
        // assert result object
        assertThat(cycle)
                .extracting(Cycle::getName)
                .isEqualTo(name);

        // assert data persisted
        UUID id = cycle.getId();
        Cycle found = em.find(Cycle.class, id);
        assertThat(found).isNotNull();
        assertThat(cycle.getName()).isEqualTo(name);
        assertThat(cycle.getMembers()).hasSize(1);
        assertThat(cycle.getMembers().get(0))
                .extracting(CycleMember::getName)
                .isEqualTo(userName);
        assertThat(cycle.getMembers().get(0))
                .extracting(CycleMember::getUserId)
                .isEqualTo(userId);

        // assert published event
    }
}