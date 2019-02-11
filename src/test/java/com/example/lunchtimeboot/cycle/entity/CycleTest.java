package com.example.lunchtimeboot.cycle.entity;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CycleTest {

    private Cycle cycle;
    private CycleMember cycleMember;

    @Before
    public void setUp() {
        UUID memberId = UUID.fromString("22222222-2222-2222-2222-000000000000");
        String memberName = "tee.tan";
        UUID memberUserId = UUID.fromString("99999999-9999-9999-9999-000000000000");
        cycleMember = new CycleMember(memberId, memberName, memberUserId);

        UUID id = UUID.fromString("11111111-1111-1111-1111-000000000000");
        String name = "Luncheon Gangster";
        cycle = new Cycle(id, name, cycleMember);
        cycle.clearEvents();
    }

    @Test
    public void join() throws CycleGuardException {
        /* given */
        String memberName = "johari.sri";
        UUID memberUserId = UUID.fromString("99999999-9999-9999-9999-111111111111");

        /* when */
        cycle.join(memberName, memberUserId);

        /* then */
        // assert change
        List<CycleMember> members = cycle.getMembers();
        assertThat(members.size())
                .isEqualTo(2);
        assertThat(members.get(1).getName())
                .isEqualTo(memberName);
    }


    @Test
    public void joinAlreadyJoinedCycle() {
        /* given */

        /* when */
        try {
            cycle.join(cycleMember.getName(), cycleMember.getUserId());
        } catch (CycleGuardException $e) {
            /* then */
            assertThat($e).isInstanceOf(CycleGuardException.class);
            assertThat($e.getCode()).isEqualTo(CycleGuardException.MEMBER_JOIN_ALREADY_JOINED);
            assertThat($e.getMessage()).contains(cycle.getId().toString());
            assertThat($e.getMessage()).contains(cycleMember.getName());
            assertThat($e.getMessage()).contains(cycle.getName());
        }
    }
}