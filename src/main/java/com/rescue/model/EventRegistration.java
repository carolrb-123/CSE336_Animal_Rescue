package com.rescue.model;

import java.time.LocalDateTime;

public class EventRegistration implements Entity {

    private int id;
    private final int eventId;
    private final int volunteerId;
    private final String status;
    private final LocalDateTime registeredAt;

    public EventRegistration(int id, int eventId, int volunteerId, String status,
                             LocalDateTime registeredAt) {
        this.id = id;
        this.eventId = eventId;
        this.volunteerId = volunteerId;
        this.status = status;
        this.registeredAt = registeredAt;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public int getVolunteerId() {
        return volunteerId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
}
