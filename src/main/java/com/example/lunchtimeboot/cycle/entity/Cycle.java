package com.example.lunchtimeboot.cycle.entity;

import com.example.lunchtimeboot.cycle.event.CycleJoinedEvent;
import com.example.lunchtimeboot.infrastructure.ddd.BaseAggregate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Cycle extends BaseAggregate {

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "cycle", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CycleMember> members = new ArrayList<>();

    public Cycle() {
    }

    public Cycle(UUID id, String name, CycleMember member) {
        this.id = id;
        this.name = name;
        this.members.add(member);
    }

    public Cycle join(String userName, UUID userId) throws CycleGuardException {
        UUID memberId = UUID.randomUUID();
        CycleMember newMember = new CycleMember(memberId, userName, userId);
        guardJoin(newMember);

        members.add(newMember);
        newMember.setCycle(this);

        addEvent(new CycleJoinedEvent(this, id, name, userName, userId));

        return this;
    }

    private void guardJoin(CycleMember newMember) throws CycleGuardException {
        UUID newMemberId = newMember.getUserId();

        for (CycleMember member: members) {
            if (member.getUserId().equals(newMemberId)) {
                throw CycleGuardException.createJoinAlreadyJoined(this, newMember);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CycleMember> getMembers() {
        return members;
    }

    public void setMembers(List<CycleMember> members) {
        this.members = members;
    }
}
