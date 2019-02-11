package com.example.lunchtimeboot.infrastructure.clock;

import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class SystemClock {
    private Clock clock = Clock.systemDefaultZone();

    public Clock getClock() {
        return clock;
    }

    public SystemClock setClock(Clock clock) {
        this.clock = clock;

        return this;
    }
}
