package com.rescue.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;
import com.rescue.model.Entity;
import com.rescue.service.Observer;
import com.rescue.service.Subject;

public class LookupTable<T extends Entity> implements Repository<T> {

    protected final Map<Integer, T> items;
    private final Subject<RepositoryEvent<T>> changes = new Subject<>();

    public LookupTable(Map<Integer, T> items) {
        this.items = Objects.requireNonNull(items, "items");
    }

    public void addObserver(Observer<RepositoryEvent<T>> observer) {
        changes.attach(observer);
    }

    public void removeObserver(Observer<RepositoryEvent<T>> observer) {
        changes.detach(observer);
    }

    public synchronized int nextId() {
        return items.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
    }

    @Override
    public void save(T item) {
        items.put(item.getId(), item);
        changes.publish(new RepositoryEvent<>(RepositoryEvent.Operation.SAVED, item));
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
        T removed = items.remove(id);
        if (removed != null) {
            changes.publish(new RepositoryEvent<>(RepositoryEvent.Operation.DELETED, removed));
        }
    }
}
