package com.rescue.service.strategy;

import com.rescue.model.Volunteer;
import com.rescue.model.VolunteerOptions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VolunteerMatchingStrategyTest {

    private final List<Volunteer> volunteers = List.of(
            new Volunteer(1, "Maya", "maya@example.com", "0101",
                    List.of(VolunteerOptions.SKILLS.get(0), VolunteerOptions.SKILLS.get(4)),
                    List.of(VolunteerOptions.AVAILABILITIES.get(0),
                            VolunteerOptions.AVAILABILITIES.get(2)), List.of("First Aid")),
            new Volunteer(2, "Omar", "omar@example.com", "0102",
                    List.of(VolunteerOptions.SKILLS.get(2)),
                    List.of(VolunteerOptions.AVAILABILITIES.get(1)), List.of()),
            new Volunteer(3, "Salma", "salma@example.com", "0103",
                    List.of(VolunteerOptions.SKILLS.get(0)),
                    List.of(VolunteerOptions.AVAILABILITIES.get(2)), List.of("Animal Care")));

    @Test
    void skillStrategyMatchesSkillWithoutCaseSensitivity() {
        List<Volunteer> matches =
                new SkillMatchingStrategy().match(volunteers, VolunteerOptions.SKILLS.get(0));

        assertEquals(List.of(1, 3), matches.stream().map(Volunteer::getId).toList());
    }

    @Test
    void availabilityStrategyMatchesAvailabilityPhraseWithoutCaseSensitivity() {
        List<Volunteer> matches =
                new AvailabilityMatchingStrategy().match(volunteers, VolunteerOptions.AVAILABILITIES.get(1));

        assertEquals(List.of(2), matches.stream().map(Volunteer::getId).toList());
    }

    @Test
    void availabilityStrategyMatchesAnySelectedVolunteerSlot() {
        AvailabilityMatchingStrategy strategy = new AvailabilityMatchingStrategy();

        assertEquals(List.of(1), strategy
                .match(volunteers, VolunteerOptions.AVAILABILITIES.get(0))
                .stream().map(Volunteer::getId).toList());
        assertEquals(List.of(1, 3), strategy
                .match(volunteers, VolunteerOptions.AVAILABILITIES.get(2))
                .stream().map(Volunteer::getId).toList());
    }

    @Test
    void strategiesReturnNoMatchesForUnmatchedCriteria() {
        assertEquals(0, new SkillMatchingStrategy().match(volunteers, "Veterinary").size());
        assertEquals(0, new AvailabilityMatchingStrategy().match(volunteers, "Overnight").size());
    }
}
