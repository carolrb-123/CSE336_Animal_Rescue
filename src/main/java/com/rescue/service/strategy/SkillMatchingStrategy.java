package com.rescue.service.strategy;

import com.rescue.model.Volunteer;

import java.util.List;

public class SkillMatchingStrategy implements VolunteerMatchingStrategy {

    @Override
    public String getName() {
        return "Match by skill";
    }

    @Override
    public List<Volunteer> match(List<Volunteer> volunteers, String criterion) {
        String skill = criterion.trim();
        return volunteers.stream()
                .filter(volunteer -> volunteer.getSkills().stream()
                        .anyMatch(existing -> existing.equalsIgnoreCase(skill)))
                .toList();
    }
}
