package com.example.lunchtimeboot.cycle.application;

import com.example.lunchtimeboot.infrastructure.activemq.Producer;
import com.example.lunchtimeboot.infrastructure.ddd.Event;
import com.example.lunchtimeboot.cycle.entity.Propose;
import com.example.lunchtimeboot.cycle.event.Constant;
import com.example.lunchtimeboot.cycle.factory.ProposeFactory;
import com.example.lunchtimeboot.cycle.repository.ProposeRepository;
import com.example.lunchtimeboot.infrastructure.clock.SystemClock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class MakeProposeApplication {
    private ApplicationEventPublisher applicationEventPublisher;
    private Producer messageQueueProducer;
    private SystemClock systemClock;
    private ProposeFactory proposeFactory;
    private ProposeRepository proposeRepository;

    @Autowired
    public MakeProposeApplication(
            ApplicationEventPublisher applicationEventPublisher,
            Producer messageQueueProducer,
            SystemClock systemClock,
            ProposeFactory proposeFactory,
            ProposeRepository proposeRepository
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageQueueProducer = messageQueueProducer;
        this.systemClock = systemClock;
        this.proposeFactory = proposeFactory;
        this.proposeRepository = proposeRepository;
    }

    public Propose makePropose(UUID userId, UUID restaurantId, LocalDate forDate) throws ProposeApplicationException, JsonProcessingException {
        /* validate */
        Instant now = systemClock.getClock().instant();
        LocalDate today = now.atZone(ZoneId.systemDefault()).toLocalDate();
        if (forDate.isBefore(today)) {
            throw ProposeApplicationException.proposeBackDate(userId, restaurantId, forDate);
        }

        Optional<Propose> alreadyProposed = proposeRepository.findUserProposeForDate(userId, forDate);
        if (alreadyProposed.isPresent()) {
            UUID proposedRestaurantId = alreadyProposed.get().getRestaurantId();
            throw ProposeApplicationException.proposedForGivenDate(userId, proposedRestaurantId, forDate);
        }

        /* perform */
        Propose propose = proposeFactory.create(userId, restaurantId);

        /* save */
        proposeRepository.save(propose);

        /* dispatch events */
        // local event dispatcher
        propose.getEvents().forEach(e -> applicationEventPublisher.publishEvent(e));

        // message queue
        ObjectMapper objectMapper = new ObjectMapper();
        for (Event e: propose.getEvents()) {
            String eventsJson = objectMapper.writeValueAsString(e);
            messageQueueProducer.publish(Constant.PRODUCER_DESTINATION_PROPOSE, eventsJson);
            messageQueueProducer.publish(Constant.PRODUCER_DESTINATION_PROPOSE_MADE, eventsJson);
        }

        return propose;
    }
}
