package com.rescue.repo;

import com.rescue.model.Adopter;

public class AdopterRepository extends LookupTable<Adopter> {
	private int nextId = 1;

	public int nextId() {
		while (findById(nextId).isPresent()) {
			nextId++;
		}

		return nextId++;
	}

}