package com.example.lunchtimeboot.cycle.factory;

import com.example.lunchtimeboot.cycle.entity.Propose;
import com.example.lunchtimeboot.infrastructure.clock.SystemClock;
import com.example.lunchtimeboot.infrastructure.uuid.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class ProposeFactory {

    private UuidGenerator uuidGenerator;
    private SystemClock systemClock;

    @Autowired
    public ProposeFactory(UuidGenerator uuidGenerator, SystemClock systemClock) {
        this.uuidGenerator = uuidGenerator;
        this.systemClock = systemClock;
    }

    public Propose create(UUID userId, UUID restaurantId) {
        UUID id = uuidGenerator.createUuid4();
        Clock clock = systemClock.getClock();
        LocalDate today = LocalDate.now(clock);

        Propose propose = new Propose(id, userId, restaurantId, today);

        return propose;
    }
}
