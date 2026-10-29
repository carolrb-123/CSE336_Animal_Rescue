package com.rescue.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.rescue.model.Entity;

public class LookupTable<T extends Entity> implements Repository<T> {

    private final Map<Integer, T> items = new HashMap<>();

    @Override
    public void save(T item) {
        items.put(item.getId(), item);
    }

    @Override
    public Optional<T> findById(int id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void delete(int id) {
        items.remove(id);
    }
}