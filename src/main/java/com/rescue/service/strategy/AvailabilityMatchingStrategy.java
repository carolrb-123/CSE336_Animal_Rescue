package com.rescue.service.strategy;

import com.rescue.model.Volunteer;

import java.util.List;

public class AvailabilityMatchingStrategy implements VolunteerMatchingStrategy {

    @Override
    public String getName() {
        return "Match by availability";
    }

    @Override
    public List<Volunteer> match(List<Volunteer> volunteers, String criterion) {
        String availability = criterion.trim();
        return volunteers.stream()
                .filter(volunteer -> volunteer.getAvailability()
                        .toLowerCase()
                        .contains(availability.toLowerCase()))
                .toList();
    }
}
