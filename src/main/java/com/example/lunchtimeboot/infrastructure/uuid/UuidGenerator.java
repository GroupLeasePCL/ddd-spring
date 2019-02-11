package com.example.lunchtimeboot.infrastructure.uuid;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidGenerator {

    public UUID createUuid4() {
        UUID uuid = UUID.randomUUID();

        return uuid;
    }
}
