package com.rescue.repo;

import com.rescue.model.Animal;

public class AnimalRepository extends LookupTable<Animal> {

	private int nextId = 1;

	public int nextId() {
		while (findById(nextId).isPresent()) {
			nextId++;
		}

		return nextId++;
	}
}