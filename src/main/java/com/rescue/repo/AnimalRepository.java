package com.rescue.repo;

import com.rescue.model.Animal;
import com.rescue.model.DataStore;

public class AnimalRepository extends LookupTable<Animal> {
    public AnimalRepository() {
        super(DataStore.getInstance().getAnimals());
    }
}