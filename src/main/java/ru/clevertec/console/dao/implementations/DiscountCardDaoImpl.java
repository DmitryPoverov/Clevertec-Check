package ru.clevertec.console.dao.implementations;

import org.springframework.stereotype.Component;
import ru.clevertec.console.utils.DaoUtil;
import ru.clevertec.console.dao.daoInterface.DiscountCardDao;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.connection.ConnectionManager;
import ru.clevertec.console.connection.ProxyConnection;
import ru.clevertec.console.validators.PageValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DiscountCardDaoImpl implements DiscountCardDao {

    private static final String FIND_ALL = """
            SELECT id, number
            FROM check_discount_card
            ORDER BY id
            LIMIT ?
            OFFSET ?
            """;
    private static final String FIND_BY_ID = """
            SELECT id, number
            FROM check_discount_card
            where id = ?
            """;
    private static final String FIND_BY_NAME = """
            SELECT id, number
            FROM check_discount_card
            where number = ?
            """;
    private static final String DELETE_BY_ID = """
            DELETE FROM check_discount_card
            where id = ?
            """;
    private static final String UPDATE_ENTITY = """
            UPDATE check_discount_card
            SET number=?
            WHERE id=?
            """;
    private static final String SAVE_NEW_ENTITY = """
            INSERT INTO check_discount_card (number)
            VALUES (?)
            """;
    private static final String COUNT_ROWS = """
            SELECT count(number)
            FROM check_discount_card
            """;

    @Override
    public List<DiscountCard> findAll(long pageSize, long pageNumber) throws SQLException {

        ArrayList<DiscountCard> resultCardList = new ArrayList<>();
        long allRows = countAllRows();
        double maxPageNumber = 0.0;
        long neededOffset;

        pageSize = PageValidator.checkAndReturnCardPageSize(pageSize);
        pageNumber = PageValidator.checkAndReturnPageNumber(pageNumber);
        maxPageNumber = PageValidator.checkAndReturnMaxPageNumber(pageSize, allRows, maxPageNumber);
        neededOffset = DaoUtil.getNeededOffset(pageSize, pageNumber);

        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {


            if (PageValidator.isSizeAndNumberAndRowNumberValid(pageSize, pageNumber, allRows, maxPageNumber)) {
                preparedStatement.setLong(1, pageSize);
                preparedStatement.setLong(2, neededOffset);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    resultCardList.add(DiscountCard.builder()
                            .id(resultSet.getLong("id"))
                            .number(resultSet.getString("number"))
                            .build());
                }
            } else {
                throw new RuntimeException("The table is empty or Wrong page number");
            }
            return resultCardList;
        }
    }

    @Override
    public long countAllRows() throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ROWS)) {
            long rows = 0;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rows = resultSet.getLong(1);
            }
            return rows;
        }
    }

    @Override
    public Optional<DiscountCard> findById(long id) throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return handleCardResultSet(resultSet);
        }
    }

    @Override
    public Optional<DiscountCard> findByName(String name) throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return handleCardResultSet(resultSet);
        }
    }


    private Optional<DiscountCard> handleCardResultSet(ResultSet resultSet) throws SQLException {
        DiscountCard result = null;
        while (resultSet.next()) {
            if (resultSet.getLong("id") != 0) {
                result = DiscountCard.builder().build();
                result.setId(resultSet.getLong("id"));
                result.setNumber(resultSet.getString("number"));
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public boolean deleteById(long id) throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        }
    }

    @Override
    public boolean update(DiscountCard entity) throws SQLException {
        try (ProxyConnection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement statement = connection.prepareStatement(UPDATE_ENTITY)) {
            statement.setString(1, entity.getNumber());
            statement.setLong(2, entity.getId());
            int result = statement.executeUpdate();
            return result == 1;
        }
    }

    @Override
    public DiscountCard save(DiscountCard entity) throws SQLException {
        try (ProxyConnection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement statement = connection.prepareStatement(SAVE_NEW_ENTITY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getNumber());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            entity.setId(keys.getLong("id"));
            return entity;
        }
    }
}
