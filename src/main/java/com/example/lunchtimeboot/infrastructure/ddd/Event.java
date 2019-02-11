package com.example.lunchtimeboot.infrastructure.ddd;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;

public abstract class Event {
    protected Map<String, Object> jsonProperties;

    @JsonAnyGetter
    public Map<String, Object> getJsonProperties() {
        return jsonProperties;
    }
}
