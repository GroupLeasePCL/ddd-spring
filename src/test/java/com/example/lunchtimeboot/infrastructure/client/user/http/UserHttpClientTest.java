package com.example.lunchtimeboot.infrastructure.client.user.http;

import com.example.lunchtimeboot.infrastructure.client.user.entity.User;
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

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.test.properties")
public class UserHttpClientTest {

    private static final String mockServerUrl = "localhost";
    private static final int mockServerPort = 38080;

    private ClientAndServer mockServer;

    @Autowired
    private UserHttpClient userHttpClient;

    private UUID userId;
    private String userName;
    private String userEmail;
    private String userMobile;

    @Before
    public void setUp() {
        userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        userName = "tee.tan";
        userEmail = "tee.tan@email.com";
        userMobile = "0811111111";
    }

    @Before
    public void startMockServer() {
        mockServer = startClientAndServer(mockServerPort);
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void getUser() throws IOException, JSONException {
        new MockServerFixture().mockServerGetUser(mockServerUrl, mockServerPort);
        User user = userHttpClient.getUser(userId);
        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getName()).isEqualTo(userName);
        assertThat(user.getEmail()).isEqualTo(userEmail);
        assertThat(user.getMobile()).isEqualTo(userMobile);
    }
}
