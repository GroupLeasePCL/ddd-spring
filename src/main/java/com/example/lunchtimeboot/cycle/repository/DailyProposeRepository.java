package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.readmodel.DailyPropose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DailyProposeRepository extends JpaRepository<DailyPropose, UUID> {
}
