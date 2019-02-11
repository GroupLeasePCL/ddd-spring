package com.example.lunchtimeboot.cycle.event.handler;

import com.example.lunchtimeboot.cycle.entity.CycleMember;
import com.example.lunchtimeboot.cycle.event.CycleJoinedEvent;
import com.example.lunchtimeboot.infrastructure.client.user.entity.User;
import com.example.lunchtimeboot.infrastructure.client.user.http.UserHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UpdateDailyProposeHandler {
    private UserHttpClient userHttpClient;

    @Autowired
    public UpdateDailyProposeHandler(UserHttpClient userHttpClient) {
        this.userHttpClient = userHttpClient;
    }

    @EventListener
    public void sendNewMemberNotification(CycleJoinedEvent event) throws IOException {
        List<CycleMember> members = event.getCycle().getMembers();

        // better use async/future, best together with get bulk user method
        String cycleName = event.getCycle().getName();
        for (CycleMember member : members) {
            // skip the just joined user, send only to other existing members
            if (member.getUserId().equals(event.getUserId())) {
                continue;
            }
            User user = userHttpClient.getUser(member.getUserId());
            String message = String.format("%s joined your group \"%s\"", user.getName(), cycleName);
            System.out.println(message);
        }
    }
}
