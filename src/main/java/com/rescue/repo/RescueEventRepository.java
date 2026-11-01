package com.rescue.repo;

import com.rescue.model.DataStore;
import com.rescue.model.RescueEvent;

public class RescueEventRepository extends LookupTable<RescueEvent> {

    public RescueEventRepository() {
        super(DataStore.getInstance().getEvents());
    }

    @Override
    public void save(RescueEvent event) {
        DataStore store = DataStore.getInstance();
        if (event.getId() <= 0) {
            event.setId(store.allocateEventId());
        } else {
            store.reserveEventId(event.getId());
        }
        super.save(event);
    }
}
