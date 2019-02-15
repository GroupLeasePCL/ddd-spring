package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.readmodel.DailyPropose;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyProposeRepositoryCustom {
    Optional<DailyPropose> findCycleProposedRestaurantForDate(UUID cycleId, UUID restaurantId, LocalDate date);
}
