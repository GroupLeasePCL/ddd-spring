package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.Cycle;

import java.util.List;
import java.util.UUID;

public interface CycleRepositoryCustom {
    List<Cycle> searchByName(String search);
    List<Cycle> findByMemberUserId(UUID userId);
}
