package com.rescue.service.strategy;

import com.rescue.model.Volunteer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VolunteerMatchingStrategyTest {

    private final List<Volunteer> volunteers = List.of(
            new Volunteer(1, "Maya", "maya@example.com", "0101",
                    List.of("First Aid", "Transport"), "Weekends", "Transporter", 12, List.of("First Aid")),
            new Volunteer(2, "Omar", "omar@example.com", "0102",
                    List.of("Fostering"), "Weekday evenings", "Foster", 4, List.of()),
            new Volunteer(3, "Salma", "salma@example.com", "0103",
                    List.of("first aid"), "Weekday mornings", "Shelter helper", 8, List.of("Animal Care")));

    @Test
    void skillStrategyMatchesSkillWithoutCaseSensitivity() {
        List<Volunteer> matches = new SkillMatchingStrategy().match(volunteers, "FIRST AID");

        assertEquals(List.of(1, 3), matches.stream().map(Volunteer::getId).toList());
    }

    @Test
    void availabilityStrategyMatchesAvailabilityPhraseWithoutCaseSensitivity() {
        List<Volunteer> matches =
                new AvailabilityMatchingStrategy().match(volunteers, "WEEKDAY");

        assertEquals(List.of(2, 3), matches.stream().map(Volunteer::getId).toList());
    }

    @Test
    void strategiesReturnNoMatchesForUnmatchedCriteria() {
        assertEquals(0, new SkillMatchingStrategy().match(volunteers, "Veterinary").size());
        assertEquals(0, new AvailabilityMatchingStrategy().match(volunteers, "overnight").size());
    }
}
