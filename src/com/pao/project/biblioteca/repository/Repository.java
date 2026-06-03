package com.pao.project.biblioteca.repository;

import java.util.List;

public interface Repository<T, ID> {
    void save(T entity);
    void delete(ID id);
    List<T> findAll();
}