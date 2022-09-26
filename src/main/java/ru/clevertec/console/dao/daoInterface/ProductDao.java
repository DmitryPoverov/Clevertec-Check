package ru.clevertec.console.dao.daoInterface;

import ru.clevertec.console.entities.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductDao {

    List<Product> findAll(long pageSize, long pageNumber) throws SQLException;

    long countAllRows() throws SQLException;

    Optional<Product> findById(long id) throws SQLException;

    Optional<Product> findByName(String name) throws SQLException;

    boolean deleteById(long id) throws SQLException;

    boolean update(Product entity) throws SQLException;

    Product save(Product entity) throws SQLException;
}
