package com.rescue.service.observer;

import com.rescue.model.Recipient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Published by the Animals module (E) on a Subject&lt;AnimalStatusChanged&gt; when an animal's status changes.
 * Statuses are text (pass yourEnum.name()), so this does not depend on E's status type,
 * which becomes the State pattern in Phase 2.
 *
 * @param interested adopters, fosters or sponsors to tell (any class that implements Recipient). May be empty.
 */
public record AnimalStatusChanged(int animalId, String animalName, String oldStatus, String newStatus,
                                  List<? extends Recipient> interested, LocalDateTime occurredAt) {

    public AnimalStatusChanged {
        Objects.requireNonNull(animalName, "animalName");
        Objects.requireNonNull(newStatus, "newStatus");
        oldStatus = oldStatus == null ? "NONE" : oldStatus;
        interested = interested == null ? List.of() : List.copyOf(interested);
        occurredAt = occurredAt == null ? LocalDateTime.now() : occurredAt;
    }

    public AnimalStatusChanged(int animalId, String animalName, String oldStatus, String newStatus,
                               List<? extends Recipient> interested) {
        this(animalId, animalName, oldStatus, newStatus, interested, LocalDateTime.now());
    }

    public boolean isNow(String status) {
        return newStatus.equalsIgnoreCase(status);
    }

    public String summary() {
        return animalName + " is now " + readable(newStatus) + " (was " + readable(oldStatus) + ")";
    }

    public static String readable(String status) {
        return status.toLowerCase().replace('_', ' ');
    }
}
