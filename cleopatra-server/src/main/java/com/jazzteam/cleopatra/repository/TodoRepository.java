package com.jazzteam.cleopatra.repository;


import com.jazzteam.cleopatra.entity.Todo;

import java.util.UUID;

public interface TodoRepository extends CrudRepository<UUID, Todo> {
}
