package com.rescue.repo;

import com.rescue.model.Entity;

public record RepositoryEvent<T extends Entity>(Operation operation, T entity) {

    public enum Operation {
        SAVED,
        DELETED
    }
}