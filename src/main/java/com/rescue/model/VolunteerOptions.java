package com.rescue.model;

import java.util.List;

public final class VolunteerOptions {

    public static final List<String> SKILLS = List.of(
            "Basic Handling",
            "Advanced Behavior & Fear Handling",
            "Medication Administration",
            "Grooming",
            "Animal Transport & Driving",
            "Adoption Matchmaking & Counseling");

    public static final List<String> AVAILABILITIES = List.of(
            "Morning (AM) — 8:00 AM - 12:00 PM",
            "Midday / Afternoon (PM) — 12:00 PM - 4:00 PM",
            "Evening (EVE) — 4:00 PM - 8:00 PM");

    private VolunteerOptions() {
    }
}
