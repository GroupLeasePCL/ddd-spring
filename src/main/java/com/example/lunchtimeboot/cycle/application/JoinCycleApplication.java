package com.example.lunchtimeboot.cycle.application;

import com.example.lunchtimeboot.cycle.entity.Cycle;
import com.example.lunchtimeboot.cycle.entity.CycleGuardException;
import com.example.lunchtimeboot.cycle.repository.CycleRepository;
import com.example.lunchtimeboot.infrastructure.client.user.entity.User;
import com.example.lunchtimeboot.infrastructure.client.user.http.UserHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class JoinCycleApplication {
    private ApplicationEventPublisher applicationEventPublisher;
    private CycleRepository cycleRepository;
    private UserHttpClient userHttpClient;

    @Autowired
    public JoinCycleApplication(
            ApplicationEventPublisher applicationEventPublisher,
            CycleRepository cycleRepository,
            UserHttpClient userHttpClient
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.cycleRepository = cycleRepository;
        this.userHttpClient = userHttpClient;
    }

    public Cycle joinCycle(UUID cycleId, UUID userId) throws IOException, CycleApplicationException, CycleGuardException {
        /* validate */

        /* perform */
        User user = userHttpClient.getUser(userId);

        Optional<Cycle> found = cycleRepository.findById(cycleId);

        /* save */
        if (!found.isPresent()) {
            throw CycleApplicationException.createCycleNotFound(cycleId);
        }

        Cycle cycle = found.get().join(user.getName(), userId);

        /* save */
        cycleRepository.save(cycle);

        /* dispatch events */
        // local Event Dispatcher
        cycle.getEvents().forEach(e -> applicationEventPublisher.publishEvent(e));

        return cycle;
    }
}
