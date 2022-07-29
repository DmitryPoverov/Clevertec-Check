package ru.clevertec.jdbc.dao.implementations;

import ru.clevertec.jdbc.dao.daoInterface.Dao;
import ru.clevertec.jdbc.entities.DiscountCard;
import ru.clevertec.jdbc.utils.DBConnection;
import ru.clevertec.jdbc.utils.ProxyConnection;

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
            SELECT *
            FROM check_discount_card
            ORDER BY id
            """;
    private static final String FIND_BY_ID = """
            SELECT *
            FROM check_discount_card
            where id = ?
            """;
    private static final String FIND_BY_NAME = """
            SELECT *
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


    @Override
    public List<DiscountCard> findAll() throws SQLException {
        try (Connection connection = new ProxyConnection(DBConnection.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<DiscountCard> cardList = new ArrayList<>();
            while (resultSet.next()) {
                cardList.add(new DiscountCard(
                        resultSet.getInt("id"),
                        resultSet.getInt("number"))
                );
            }
            return cardList;
        }
    }

    @Override
    public Optional<DiscountCard> findById(Integer id) throws SQLException {
        try (Connection connection = new ProxyConnection(DBConnection.getConnection());
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
        try (Connection connection = new ProxyConnection(DBConnection.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            DiscountCard discountCard = new DiscountCard();
            handleResultSet(resultSet, discountCard);
            return Optional.of(discountCard);
        }
    }

    private void handleResultSet(ResultSet resultSet, DiscountCard discountCard) throws SQLException {
        if (resultSet.next()) {
            discountCard.setId(resultSet.getInt("id"));
            discountCard.setNumber(resultSet.getInt("number"));
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        try (Connection connection = new ProxyConnection(DBConnection.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            return result == 1;
        }
    }

    @Override
    public boolean update(DiscountCard entity) throws SQLException {
        try (ProxyConnection connection = new ProxyConnection(DBConnection.getConnection());
             PreparedStatement statement = connection.prepareStatement(UPDATE_ENTITY)) {
            statement.setInt(1, entity.getNumber());
            statement.setInt(2, entity.getId());
            int result = statement.executeUpdate();
            return result==1;
        }
    }

    @Override
    public DiscountCard save(DiscountCard entity) throws SQLException {
        try (ProxyConnection connection = new ProxyConnection(DBConnection.getConnection());
             PreparedStatement statement = connection.prepareStatement(SAVE_NEW_ENTITY, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, entity.getNumber());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            entity.setId(keys.getInt("id"));
            return entity;
        }
    }
}
