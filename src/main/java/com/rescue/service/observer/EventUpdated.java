package com.rescue.service.observer;

import com.rescue.model.Recipient;

import java.util.ArrayList;
import java.util.List;

// The news the Events module publishes when an event changes.
// Example: "Adoption Day" -> "Moved to Sat 7 Nov, 10:00".
public class EventUpdated {

    private final int eventId;
    private final String title;
    private final String change;
    private final List<Recipient> participants;   // people registered for the event

    public EventUpdated(int eventId, String title, String change, List<Recipient> participants) {
        this.eventId = eventId;
        this.title = title;
        this.change = change;
        if (participants == null) {
            this.participants = new ArrayList<>();
        } else {
            this.participants = participants;
        }
    }

    public int getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getChange() {
        return change;
    }

    public List<Recipient> getParticipants() {
        return participants;
    }

    public String getSummary() {
        return "Event \"" + title + "\": " + change;
    }
}
