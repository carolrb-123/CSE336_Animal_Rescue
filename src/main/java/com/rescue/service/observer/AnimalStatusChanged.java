package com.rescue.service.observer;

import com.rescue.model.Recipient;

import java.util.ArrayList;
import java.util.List;

// The news the Animals module publishes when an animal's status changes.
// Example: "Bondok" went from "IN_TREATMENT" to "AVAILABLE".
public class AnimalStatusChanged {

    private final int animalId;
    private final String animalName;
    private final String oldStatus;
    private final String newStatus;
    private final List<Recipient> interested;   // people following this animal (can be empty)

    public AnimalStatusChanged(int animalId, String animalName, String oldStatus, String newStatus,
                               List<Recipient> interested) {
        this.animalId = animalId;
        this.animalName = animalName;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        if (interested == null) {
            this.interested = new ArrayList<>();
        } else {
            this.interested = interested;
        }
    }

    public int getAnimalId() {
        return animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public List<Recipient> getInterested() {
        return interested;
    }

    public String getSummary() {
        return animalName + " is now " + newStatus + " (was " + oldStatus + ")";
    }
}
