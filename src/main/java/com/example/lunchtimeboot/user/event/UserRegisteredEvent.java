package com.example.lunchtimeboot.user.event;

import com.example.lunchtimeboot.infrastructure.ddd.Event;
import com.example.lunchtimeboot.user.entity.User;

import java.util.UUID;

public class UserRegisteredEvent extends Event {
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userMobile;

    public UserRegisteredEvent(UUID userId, String userName, String userEmail, String userMobile) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
