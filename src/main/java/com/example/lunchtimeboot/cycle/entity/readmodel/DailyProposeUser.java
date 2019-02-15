package com.example.lunchtimeboot.cycle.entity.readmodel;

import com.example.lunchtimeboot.infrastructure.ddd.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class DailyProposeUser extends BaseEntity {

    @ManyToOne
    private DailyPropose dailyPropose;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String userName;

    public DailyProposeUser() {
    }

    public DailyProposeUser(UUID id, UUID userId, String userName) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
    }

    public DailyPropose getDailyPropose() {
        return dailyPropose;
    }

    public void setDailyPropose(DailyPropose dailyPropose) {
        this.dailyPropose = dailyPropose;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
