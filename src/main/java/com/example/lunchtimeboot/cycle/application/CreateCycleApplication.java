package com.example.lunchtimeboot.cycle.application;

import com.example.lunchtimeboot.cycle.entity.Cycle;
import com.example.lunchtimeboot.cycle.factory.CycleFactory;
import com.example.lunchtimeboot.cycle.repository.CycleRepository;
import com.example.lunchtimeboot.infrastructure.client.user.entity.User;
import com.example.lunchtimeboot.infrastructure.client.user.http.UserHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class CreateCycleApplication {
    private CycleFactory cycleFactory;
    private CycleRepository cycleRepository;
    private UserHttpClient userHttpClient;

    @Autowired
    public CreateCycleApplication(CycleFactory cycleFactory, CycleRepository cycleRepository, UserHttpClient userHttpClient) {
        this.cycleFactory = cycleFactory;
        this.cycleRepository = cycleRepository;
        this.userHttpClient = userHttpClient;
    }

    public Cycle createCycle(String name, UUID userId) throws IOException {
        /* validate */

        /* perform */
        User user = userHttpClient.getUser(userId);

        Cycle cycle = cycleFactory.create(name, user);

        /* save */
        cycleRepository.save(cycle);

        /* dispatch events */

        return cycle;
    }
}
