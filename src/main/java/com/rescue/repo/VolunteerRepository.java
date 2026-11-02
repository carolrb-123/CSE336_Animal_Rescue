package com.rescue.repo;

import com.rescue.model.DataStore;
import com.rescue.model.Volunteer;

public class VolunteerRepository extends LookupTable<Volunteer> {

    public VolunteerRepository() {
        super(DataStore.getInstance().getVolunteers());
    }

    @Override
    public void save(Volunteer volunteer) {
        DataStore store = DataStore.getInstance();
        if (volunteer.getId() <= 0) {
            volunteer.setId(store.allocateVolunteerId());
        } else {
            store.reserveVolunteerId(volunteer.getId());
        }
        super.save(volunteer);
    }
}
