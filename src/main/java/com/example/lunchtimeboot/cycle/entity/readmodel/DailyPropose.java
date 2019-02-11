package com.example.lunchtimeboot.cycle.entity.readmodel;

import com.example.lunchtimeboot.infrastructure.ddd.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class DailyPropose extends BaseEntity {
    @Column(nullable = false)
    private UUID cycleId;

    @Embedded
    private DailyProposeRestaurant restaurant;

//    @OneToMany(mappedBy = "DailyProposeUser", cascade={CascadeType.ALL}, orphanRemoval = true)
//    private List<DailyProposeUser> proposedUsers = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate forDate;

    @Column(nullable = false)
    private Integer proposesCount;

    public DailyPropose() {
    }

    public DailyPropose(UUID cycleId, DailyProposeRestaurant restaurant, DailyProposeUser user, LocalDate forDate) {
        this.cycleId = cycleId;
        this.restaurant = restaurant;
        this.forDate = forDate;
        this.proposesCount = 0;
    }

    public UUID getCycleId() {
        return cycleId;
    }

    public void setCycleId(UUID cycleId) {
        this.cycleId = cycleId;
    }

    public DailyProposeRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(DailyProposeRestaurant restaurant) {
        this.restaurant = restaurant;
    }

//    public List<DailyProposeUser> getProposedUsers() {
//        return proposedUsers;
//    }

//    public void setProposedUsers(List<DailyProposeUser> proposedUsers) {
//        this.proposedUsers = proposedUsers;
//    }

    public LocalDate getForDate() {
        return forDate;
    }

    public void setForDate(LocalDate forDate) {
        this.forDate = forDate;
    }

    public Integer getProposesCount() {
        return proposesCount;
    }

    public void setProposesCount(Integer proposesCount) {
        this.proposesCount = proposesCount;
    }
}
