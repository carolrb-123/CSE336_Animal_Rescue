package com.rescue.repo;
import com.rescue.model.Donor;
import java.util.*;

public class DonorRepository implements Repository<Donor> {
    private final Map<Integer, Donor> donors = new LinkedHashMap<>();
    private int nextId = 1;

    @Override
    public void save(Donor donor) {
        if (donor.getId() <= 0) {
            donor.setId(nextId++);
        } else {
            nextId = Math.max(nextId, donor.getId() + 1);
        }

        donors.put(donor.getId(), donor);
    }

    @Override
    public Optional<Donor> findById(int id) {
        return Optional.ofNullable(donors.get(id));
    }

    @Override
    public List<Donor> findAll() {
        return new ArrayList<>(donors.values());
    }

    @Override
    public void delete(int id) {
        donors.remove(id);
    }
}
