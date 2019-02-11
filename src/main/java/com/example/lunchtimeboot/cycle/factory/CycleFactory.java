package com.example.lunchtimeboot.cycle.factory;

import com.example.lunchtimeboot.cycle.entity.Cycle;
import com.example.lunchtimeboot.cycle.entity.CycleMember;
import com.example.lunchtimeboot.infrastructure.client.user.entity.User;
import com.example.lunchtimeboot.infrastructure.uuid.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CycleFactory {

    private UuidGenerator uuidGenerator;

    @Autowired
    public CycleFactory(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    public Cycle create(String name, User user) {
        UUID cycleId = this.uuidGenerator.createUuid4();
        UUID memberId = this.uuidGenerator.createUuid4();

        CycleMember member = new CycleMember(memberId, user.getName(), user.getId());
        Cycle cycle = new Cycle(cycleId, name, member);

        return cycle;
    }
}
