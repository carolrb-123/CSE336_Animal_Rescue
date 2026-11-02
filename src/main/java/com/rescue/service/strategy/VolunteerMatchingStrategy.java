package com.rescue.service.strategy;

import com.rescue.model.Volunteer;

import java.util.List;

public interface VolunteerMatchingStrategy {

    String getName();

    List<Volunteer> match(List<Volunteer> volunteers, String criterion);
}
