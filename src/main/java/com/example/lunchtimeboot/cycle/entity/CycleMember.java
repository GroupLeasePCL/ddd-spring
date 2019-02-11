package com.example.lunchtimeboot.cycle.entity;

import com.example.lunchtimeboot.infrastructure.ddd.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class CycleMember extends BaseEntity {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cycle_id")
    private Cycle cycle;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID userId;

    public CycleMember() {
    }

    public CycleMember(UUID id, String name, UUID userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
