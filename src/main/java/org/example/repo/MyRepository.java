package org.example.repo;

import java.util.List;

public interface MyRepository<T> {
    List<T> retrieveAll();

    void store(T t);

    boolean removeItemById(Long id);
}
