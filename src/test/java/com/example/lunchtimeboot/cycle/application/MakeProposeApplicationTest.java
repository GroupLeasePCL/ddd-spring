package com.example.lunchtimeboot.cycle.application;

import com.example.lunchtimeboot.cycle.application.MakeProposeApplication;
import com.example.lunchtimeboot.cycle.application.ProposeApplicationException;
import com.example.lunchtimeboot.infrastructure.activemq.Producer;
import com.example.lunchtimeboot.infrastructure.clock.SystemClock;
import com.example.lunchtimeboot.infrastructure.ddd.CodedException;
import com.example.lunchtimeboot.cycle.entity.Propose;
import com.example.lunchtimeboot.cycle.event.Constant;
import com.example.lunchtimeboot.cycle.event.ProposeMadeEvent;
import com.example.lunchtimeboot.cycle.factory.ProposeFactory;
import com.example.lunchtimeboot.cycle.repository.ProposeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MakeProposeApplicationTest {
    @Autowired
    private EntityManager em;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @Captor
    protected ArgumentCaptor<Object> publishEventCaptor;

    @MockBean
    private Producer messageQueueProducer;

    @Captor
    protected ArgumentCaptor<String> producerEventCaptor;

    @Captor
    protected ArgumentCaptor<String> producerEventNameCaptor;

    @MockBean
    private SystemClock systemClock;

    @Autowired
    private ProposeFactory proposeFactory;

    @Autowired
    private ProposeRepository proposeRepository;

    private MakeProposeApplication makeProposeApplication;

    @Before
    public void setUp() {
        makeProposeApplication = new MakeProposeApplication(applicationEventPublisher, messageQueueProducer, systemClock, proposeFactory, proposeRepository);

        Clock systemTime = Clock.fixed(Instant.parse("2000-01-01T09:00:00.00Z"), ZoneId.systemDefault());
        Mockito.when(systemClock.getClock())
                .thenReturn(systemTime);
    }

    @Test
    public void makePropose() throws ProposeApplicationException, JsonProcessingException {
        /* given */
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID restaurantId = UUID.fromString("99999999-9999-9999-9999-000000000000");
        LocalDate forDate = LocalDate.of(2000, 01, 01);

        /* when */
        Propose propose = makeProposeApplication.makePropose(userId, restaurantId, forDate);

        /* then */
        // assert result object
        assertThat(propose.getUserId()).isEqualTo(userId);
        assertThat(propose.getRestaurantId()).isEqualTo(restaurantId);
        assertThat(propose.getForDate()).isEqualTo(forDate);

        // assert data persisted
        UUID id = propose.getId();
        Propose found = em.find(Propose.class, id);
        assertThat(found).isNotNull();
        assertThat(found.getUserId()).isEqualTo(userId);
        assertThat(found.getRestaurantId()).isEqualTo(restaurantId);
        assertThat(found.getForDate()).isEqualTo(forDate);

        // assert published event
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(publishEventCaptor.capture());
        List<Object> capturedEvents = publishEventCaptor.getAllValues();
        assertThat(capturedEvents).hasSize(1);
        assertThat(capturedEvents.get(0)).isInstanceOf(ProposeMadeEvent.class);

        // assert mq produced event
        Mockito.verify(messageQueueProducer, Mockito.times(2))
                .publish(producerEventNameCaptor.capture(), producerEventCaptor.capture());

        // mq: destination names
        List<String> eventNames = producerEventNameCaptor.getAllValues();
        assertThat(eventNames).hasSize(2);
        assertThat(eventNames.get(0))
                .isEqualTo(Constant.PRODUCER_DESTINATION_PROPOSE);
        assertThat(eventNames.get(1))
                .isEqualTo(Constant.PRODUCER_DESTINATION_PROPOSE_MADE);

        // mq: event json(s)
        List<String> capturedMqProducedEvents = producerEventCaptor.getAllValues();
        assertThat(capturedMqProducedEvents).hasSize(2);
    }

    @Test
    public void makeProposeBackDate() throws JsonProcessingException {
        /* given */
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID restaurantId = UUID.fromString("99999999-9999-9999-9999-000000000000");
        LocalDate forDate = LocalDate.of(1999, 01, 01);

        /* when */
        try {
            Propose propose = makeProposeApplication.makePropose(userId, restaurantId, forDate);
            fail("Expect exception: " + ProposeApplicationException.class);
        } catch (CodedException e) {
            assertThat(e).isInstanceOf(ProposeApplicationException.class);
            assertThat(e.getCode()).isEqualTo(ProposeApplicationException.PROPOSE_BACK_DATE);
        }
    }
}
