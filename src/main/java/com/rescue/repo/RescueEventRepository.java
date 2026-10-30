package com.rescue.repo;

import com.rescue.model.DataStore;
import com.rescue.model.RescueEvent;

public class RescueEventRepository extends LookupTable<RescueEvent> {

    public RescueEventRepository() {
        super(DataStore.getInstance().getEvents());
    }

    @Override
    public void save(RescueEvent event) {
        if (event.getId() <= 0) {
            event.setId(nextId());
        }
        super.save(event);
    }

    private int nextId() {
        return items.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
    }
}
