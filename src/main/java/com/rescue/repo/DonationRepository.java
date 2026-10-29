
package com.rescue.repo;

import com.rescue.model.Donation;
import java.util.*;

public class DonationRepository implements Repository<Donation> {

    private final Map<Integer, Donation> donations = new LinkedHashMap<>();
    private int nextId = 1;

    @Override
    public void save(Donation donation) {
        if (donation.getId() <= 0) {
            donation.setId(nextId++);
        } else {
            nextId = Math.max(nextId, donation.getId() + 1);
        }

        donations.put(donation.getId(), donation);
    }

    @Override
    public Optional<Donation> findById(int id) {
        return Optional.ofNullable(donations.get(id));
    }

    @Override
    public List<Donation> findAll() {
        return new ArrayList<>(donations.values());
    }

    @Override
    public void delete(int id) {
        donations.remove(id);
    }
}
