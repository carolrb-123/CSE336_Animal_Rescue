package com.rescue.service;

public interface Observer<E> {
    void update(E event);
}
