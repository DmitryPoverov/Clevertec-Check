package ru.clevertec.console.dao.daoInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductDao<K,T> {

    List<T> findAll(K pageSize, K pageNumber) throws SQLException;

    K countAllRows() throws SQLException;

    Optional<T> findById(K id) throws SQLException;

    Optional<T> findByName(String name) throws SQLException;

    String getNameById(K id) throws SQLException;

    boolean deleteById(K id) throws SQLException;

    boolean update(T entity) throws SQLException;

    T save(T entity) throws SQLException;
}
