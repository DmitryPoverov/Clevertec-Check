package ru.clevertec.console.dao.implementations;

import ru.clevertec.console.dao.daoInterface.Dao;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.utils.ConnectionManager;
import ru.clevertec.console.utils.ProxyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiscountCardDao implements Dao<Integer, DiscountCard> {

    private static final Dao<Integer, DiscountCard> INSTANCE = new DiscountCardDao();

    private DiscountCardDao() {
    }

    public static Dao<Integer, DiscountCard> getInstance() {
        return INSTANCE;
    }

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
    public List<DiscountCard> findAll(Integer pageSize, Integer pageNumber) throws SQLException {

        ArrayList<DiscountCard> cardList = new ArrayList<>();
        int rows = countAllRows();
        double maxPageNumber = 0.0;

        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            if (pageSize <= 0) {
                pageSize = 2;
            }
            if (pageNumber <= 0) {
                pageNumber = 1;
            }
            if (rows != 0) {
                maxPageNumber = (double) rows / pageSize;
                if (maxPageNumber%1 != 0) {
                    maxPageNumber = (maxPageNumber - maxPageNumber%1) + 1;
                }
            }

            if (rows == 0 && pageSize * pageNumber > maxPageNumber * pageSize) {
                throw new RuntimeException("The table is empty or Wrong page number");
            } else {
                preparedStatement.setInt(1, pageSize);
                preparedStatement.setInt(2, pageSize * pageNumber - pageSize);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    cardList.add(new DiscountCard(
                            resultSet.getInt("id"),
                            resultSet.getString("number")));
                }
            }
            return cardList;
        }
    }

    @Override
    public Integer countAllRows() throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ROWS)) {
            int rows = 0;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rows = resultSet.getInt(1);
            }
            return rows;
        }
    }

    @Override
    public Optional<DiscountCard> findById(Integer id) throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            DiscountCard discountCard = new DiscountCard();
            handleResultSet(resultSet, discountCard);
            return Optional.of(discountCard);
        }
    }

    @Override
    public Optional<DiscountCard> findByName(String name) throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            DiscountCard discountCard = new DiscountCard();
            handleResultSet(resultSet, discountCard);
            return Optional.of(discountCard);
        }
    }

    @Override
    public boolean isSuchCard(String name) throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            DiscountCard discountCard = new DiscountCard();
            handleResultSet(resultSet, discountCard);
            return discountCard.getId() != 0;
        }
    }

    @Override
    public String getNameById(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getPriceById(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDiscountById(Integer id) {
        throw new UnsupportedOperationException();
    }

    private void handleResultSet(ResultSet resultSet, DiscountCard discountCard) throws SQLException {
        if (resultSet.next()) {
            discountCard.setId(resultSet.getInt("id"));
            discountCard.setNumber(resultSet.getString("number"));
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        }
    }

    @Override
    public boolean update(DiscountCard entity) throws SQLException {
        try (ProxyConnection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement statement = connection.prepareStatement(UPDATE_ENTITY)) {
            statement.setString(1, entity.getNumber());
            statement.setInt(2, entity.getId());
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
            entity.setId(keys.getInt("id"));
            return entity;
        }
    }
}
