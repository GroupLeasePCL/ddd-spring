package com.example.lunchtimeboot.infrastructure.ddd;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public abstract class BaseAggregate extends BaseEntity {
    @JsonIgnore
    private ArrayList<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        events.add(event);
    }

    public void clearEvents() {
        events.clear();
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
