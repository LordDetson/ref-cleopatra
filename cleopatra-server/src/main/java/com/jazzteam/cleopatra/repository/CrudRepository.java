package com.jazzteam.cleopatra.repository;

import java.util.List;

public interface CrudRepository<K, T> {
    T create(T t);

    T update(K id, T t);

    T get(K id);

    List<T> getAll();

    void delete(K id);

    T getOrThrow(K id);
}