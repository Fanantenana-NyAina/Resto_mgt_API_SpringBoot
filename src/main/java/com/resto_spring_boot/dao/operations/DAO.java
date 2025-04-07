package com.resto_spring_boot.dao.operations;

import java.util.List;

public interface DAO<E> {
    List<E> getAll();

    E getById(int id);

    List<E> saveAll (List<E> entity);
}
