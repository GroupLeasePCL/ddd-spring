package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.Propose;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ProposeRepositoryCustom {
    Optional<Propose> findUserProposeForDate(UUID userId, LocalDate date);
}
