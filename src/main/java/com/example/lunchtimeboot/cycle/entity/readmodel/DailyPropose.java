package com.example.lunchtimeboot.cycle.entity.readmodel;

import com.example.lunchtimeboot.infrastructure.ddd.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class DailyPropose extends BaseEntity {
    @Embedded
    private DailyProposeCycle cycle;

    @Embedded
    private DailyProposeRestaurant restaurant;

    @OneToMany(mappedBy = "dailyPropose", cascade={CascadeType.ALL}, orphanRemoval = true)
    private List<DailyProposeUser> proposedUsers = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate forDate;

    @Column(nullable = false)
    private Integer proposeCount;

    public DailyPropose() {
    }

    public DailyPropose(DailyProposeCycle cycle, DailyProposeRestaurant restaurant, DailyProposeUser user, LocalDate forDate) {
        this.id = UUID.randomUUID();
        this.cycle = cycle;
        this.restaurant = restaurant;
        this.proposedUsers.add(user);
        user.setDailyPropose(this);
        this.forDate = forDate;
        this.proposeCount = 1;
    }

    public DailyPropose addProposedUser(UUID userId, String userName) {
        DailyProposeUser user = new DailyProposeUser(UUID.randomUUID(), userId, userName);
        proposedUsers.add(user);
        user.setDailyPropose(this);
        proposeCount = proposedUsers.size();
        return this;
    }

    public DailyPropose removeProposedUser(UUID userId) {
        for(DailyProposeUser user: proposedUsers) {
            if (user.getUserId().equals(userId)) {
                proposedUsers.remove(user);
                user.setDailyPropose(null);
            }
        }
        proposeCount = proposedUsers.size();
        return this;
    }

    public DailyProposeCycle getCycle() {
        return cycle;
    }

    public void setCycle(DailyProposeCycle cycle) {
        this.cycle = cycle;
    }

    public DailyProposeRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(DailyProposeRestaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<DailyProposeUser> getProposedUsers() {
        return proposedUsers;
    }

    public void setProposedUsers(List<DailyProposeUser> proposedUsers) {
        this.proposedUsers = proposedUsers;
    }

    public LocalDate getForDate() {
        return forDate;
    }

    public void setForDate(LocalDate forDate) {
        this.forDate = forDate;
    }

    public Integer getProposeCount() {
        return proposeCount;
    }

    public void setProposeCount(Integer proposeCount) {
        this.proposeCount = proposeCount;
    }
}
