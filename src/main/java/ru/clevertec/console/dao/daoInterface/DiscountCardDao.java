package ru.clevertec.console.dao.daoInterface;

import ru.clevertec.console.entities.DiscountCard;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DiscountCardDao {

    List<DiscountCard> findAll(long pageSize, long pageNumber) throws SQLException;

    long countAllRows() throws SQLException;

    Optional<DiscountCard> findById(long id) throws SQLException;

    Optional<DiscountCard> findByName(String name) throws SQLException;

    boolean deleteById(long id) throws SQLException;

    boolean update(DiscountCard entity) throws SQLException;

    DiscountCard save(DiscountCard entity) throws SQLException;
}
