package ru.clevertec.console.dao.implementations;

import ru.clevertec.console.dao.daoInterface.DiscountCardDao;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.serviceClass.CheckService;
import ru.clevertec.console.serviceClass.CheckServiceImpl;
import ru.clevertec.console.utils.ConnectionManager;
import ru.clevertec.console.utils.ProxyConnection;
import ru.clevertec.console.validators.PageValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiscountCardDaoImpl implements DiscountCardDao<Integer, DiscountCard> {

    private static final DiscountCardDao<Integer, DiscountCard> INSTANCE = new DiscountCardDaoImpl();
    private static final CheckService SERVICE = CheckServiceImpl.getInstance();
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

    private DiscountCardDaoImpl() {}

    public static DiscountCardDao<Integer, DiscountCard> getInstance() {
        return INSTANCE;
    }

    @Override
    public List<DiscountCard> findAll(Integer pageSize, Integer pageNumber) throws SQLException {

        ArrayList<DiscountCard> resultCardList = new ArrayList<>();
        int allRows = countAllRows();
        double maxPageNumber = 0.0;
        int neededOffset;

        pageSize = PageValidator.checkAndReturnCardPageSize(pageSize);
        pageNumber = PageValidator.checkAndReturnPageNumber(pageNumber);
        maxPageNumber = PageValidator.checkAndReturnMaxPageNumber(pageSize, allRows, maxPageNumber);
        neededOffset = SERVICE.getNeededOffset(pageSize, pageNumber);

        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {


            if (PageValidator.isSizeAndNumberAndRowNumberValid(pageSize, pageNumber, allRows, maxPageNumber)) {
                preparedStatement.setInt(1, pageSize);
                preparedStatement.setInt(2, neededOffset);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    resultCardList.add(DiscountCard.builder()
                            .id(resultSet.getInt("id"))
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
            DiscountCard discountCard = handleCardResultSet(resultSet);
            return Optional.ofNullable(discountCard);
        }
    }

    @Override
    public boolean isSuchCard(String name) throws SQLException {
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            DiscountCard discountCard = DiscountCard.builder().build();
            handleCardResultSet(resultSet, discountCard);
            return discountCard.getId() != 0;
        }
    }

    private void handleCardResultSet(ResultSet resultSet, DiscountCard discountCard) throws SQLException {
        while (resultSet.next()) {
            discountCard.setId(resultSet.getInt("id"));
            discountCard.setNumber(resultSet.getString("number"));
        }
    }


    private DiscountCard handleCardResultSet(ResultSet resultSet) throws SQLException {
        DiscountCard result = null;
        while (resultSet.next()) {
            if (resultSet.getInt("id") != 0) {
                result = DiscountCard.builder().build();
                result.setId(resultSet.getInt("id"));
                result.setNumber(resultSet.getString("number"));
            }
        }
        return result;
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
