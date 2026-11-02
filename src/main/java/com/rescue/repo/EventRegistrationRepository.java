package com.rescue.repo;

import com.rescue.model.DataStore;
import com.rescue.model.EventRegistration;

public class EventRegistrationRepository extends LookupTable<EventRegistration> {

    public EventRegistrationRepository() {
        super(DataStore.getInstance().getEventRegistrations());
    }

    @Override
    public void save(EventRegistration registration) {
        if (registration.getId() <= 0) {
            registration.setId(nextId());
        }
        super.save(registration);
    }


}
