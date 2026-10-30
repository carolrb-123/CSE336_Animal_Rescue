package com.rescue.repo;

import com.rescue.model.DataStore;
import com.rescue.model.Volunteer;

public class VolunteerRepository extends LookupTable<Volunteer> {

    public VolunteerRepository() {
        super(DataStore.getInstance().getVolunteers());
    }

    @Override
    public void save(Volunteer volunteer) {
        if (volunteer.getId() <= 0) {
            volunteer.setId(nextId());
        }
        super.save(volunteer);
    }

    private int nextId() {
        return items.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
    }
}
