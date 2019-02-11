package com.example.lunchtimeboot.cycle.entity.readmodel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class DailyProposeUser {

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String userName;

    public DailyProposeUser() {
    }

    public DailyProposeUser(UUID userId, String userName) {
        this.userId = userId;
        this.userName = userName;
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
