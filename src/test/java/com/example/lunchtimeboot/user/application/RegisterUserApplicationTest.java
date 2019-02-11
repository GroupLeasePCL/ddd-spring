package com.example.lunchtimeboot.user.application;

import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.event.UserRegisteredEvent;
import com.example.lunchtimeboot.user.factory.UserFactory;
import com.example.lunchtimeboot.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterUserApplicationTest {
    @Autowired
    private EntityManager em;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @Captor
    protected ArgumentCaptor<Object> publishEventCaptor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFactory userFactory;

    private RegisterUserApplication registerUserApplication;

    @Before
    public void setUp() {
        registerUserApplication = new RegisterUserApplication(applicationEventPublisher, userFactory, userRepository);
    }

    @Test
    public void registerNewUser() {
        /* given */
        String name = "tee.tan";
        String email = "tee.tan@mail.com";

        /* when */
        User user = registerUserApplication.registerNewUser(name, email, null);

        /* then */
        // assert result object
        assertThat(user)
                .extracting(User::getName)
                .isEqualTo(name);

        // assert data persisted
        UUID id = user.getId();
        User found = em.find(User.class, id);
        assertThat(found).isNotNull();
        assertThat(user.getName()).isEqualTo(name);
        assertThat(found.getEmail()).isEqualTo(email);

        // assert published event
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(publishEventCaptor.capture());
        List<Object> capturedEvents = publishEventCaptor.getAllValues();
        assertThat(capturedEvents).hasSize(1);
        assertThat(capturedEvents.get(0)).isInstanceOf(UserRegisteredEvent.class);
    }
}
