package com.jazzteam.cleopatra.service;

import java.util.List;

public interface CrudService <K, T> {
    T create(T t);

    T update(K id, T t);

    T get(K id);

    List<T> getAll();

    void delete(K id);
}
