package com.rescue.repo;

import com.rescue.model.Adopter;
import com.rescue.model.DataStore;

public class AdopterRepository extends LookupTable<Adopter> {
    public AdopterRepository() {
        super(DataStore.getInstance().getAdopters());
    }
}