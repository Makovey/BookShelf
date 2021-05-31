package org.example.repo;

import java.util.List;

public interface MyRepository<T> {
    List<T> getAll();

    void saveItem(T t);

    boolean removeItemById(String id);
}
