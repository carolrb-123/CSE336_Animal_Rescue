package com.rescue.service.observer;

import com.rescue.model.Recipient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Published by the Events module (D) on a Subject&lt;EventUpdated&gt; when an adoption day,
 * fundraiser or vaccination drive is moved, cancelled or needs a reminder.
 *
 * @param change       what changed, e.g. "Moved to Sat 7 Nov, 10:00"
 * @param participants registered volunteers (any class that implements Recipient)
 */
public record EventUpdated(int eventId, String title, String change, List<? extends Recipient> participants,
                           LocalDateTime occurredAt) {

    public EventUpdated {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(change, "change");
        participants = participants == null ? List.of() : List.copyOf(participants);
        occurredAt = occurredAt == null ? LocalDateTime.now() : occurredAt;
    }

    public EventUpdated(int eventId, String title, String change, List<? extends Recipient> participants) {
        this(eventId, title, change, participants, LocalDateTime.now());
    }

    public String summary() {
        return "Event \"" + title + "\": " + change;
    }
}
