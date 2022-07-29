package ru.clevertec.jdbc.dao.daoInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<K,T> {

    List<T> findAll() throws SQLException;

    Optional<T> findById(K id) throws SQLException;

    Optional<T> findByName(String name) throws SQLException;

    boolean deleteById(K id) throws SQLException;

    boolean update(T entity) throws SQLException;

    T save(T entity) throws SQLException;
}
