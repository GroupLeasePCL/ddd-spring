package com.example.lunchtimeboot.user.factory;

import com.example.lunchtimeboot.infrastructure.uuid.UuidGenerator;
import com.example.lunchtimeboot.user.entity.User;
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
public class UserFactoryTest {

    @Autowired
    private UserFactory userFactory;

    @MockBean
    private UuidGenerator uuidGenerator;

    private UUID uuid = UUID.fromString("11111111-1111-1111-1111-000000000000");

    @Before
    public void setUp() {
        Mockito.when(uuidGenerator.createUuid4())
                .thenReturn(uuid);
    }

    @Test
    public void createUser() {
        String name = "tee.tan";
        String email = "tee.tan@email.com";
        String mobile = "0810000000";

        User user = userFactory.create(name, email,  mobile);

        assertThat(user.getId()).isEqualTo(uuid);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }
}
