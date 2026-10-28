package com.rescue.repo;

import com.rescue.model.Entity;

import java.util.List;
import java.util.Optional;
public interface Repository<T extends Entity> {
    void save(T item);
    Optional<T> findById(int id);
    List<T> findAll();
    void delete(int id);
}
