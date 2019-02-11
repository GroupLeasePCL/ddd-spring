package com.example.lunchtimeboot.cycle.repository;

import com.example.lunchtimeboot.cycle.entity.Cycle;

import java.util.List;

public interface CycleRepositoryCustom {
    List<Cycle> searchByName(String search);
}
