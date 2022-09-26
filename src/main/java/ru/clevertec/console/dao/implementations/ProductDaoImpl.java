package ru.clevertec.console.dao.implementations;

import org.springframework.stereotype.Component;
import ru.clevertec.console.utils.DaoUtil;
import ru.clevertec.console.dao.daoInterface.ProductDao;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.connection.ConnectionManager;
import ru.clevertec.console.connection.ProxyConnection;
import ru.clevertec.console.validators.PageValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDaoImpl implements ProductDao {

    private static final String FIND_ALL = """
            SELECT id, title, price, discount
            FROM check_products
            ORDER BY id
            LIMIT ?
            OFFSET ?
            """;
    private static final String FIND_BY_ID = """
            SELECT id, title, price, discount
            FROM check_products
            where id = ?
            """;
    private static final String FIND_BY_NAME = """
            SELECT id, title, price, discount
            FROM check_products
            where title = ?
            """;
    private static final String SAVE_NEW_ENTITY = """
            INSERT INTO check_products (title, price, discount)
            VALUES (?, ?, ?)
            """;
    private static final String UPDATE_ENTITY = """
            UPDATE check_products
            SET title=?, price=?, discount=?
            WHERE id=?
            """;
    private static final String DELETE_BY_ID = """
            DELETE FROM check_products
            where id = ?
            """;
    private static final String COUNT_ROWS = """
            SELECT count(title)
            FROM check_products
            """;

    @Override
    public List<Product> findAll(long pageSize, long pageNumber) throws SQLException {

        ArrayList<Product> resultProductList = new ArrayList<>();
        long allRows = countAllRows();
        double maxPageNumber = 0.0;
        long neededOffset;

        pageSize = PageValidator.checkAndReturnProductPageSize(pageSize);
        pageNumber = PageValidator.checkAndReturnPageNumber(pageNumber);
        maxPageNumber = PageValidator.checkAndReturnMaxPageNumber(pageSize, allRows, maxPageNumber);
        neededOffset = DaoUtil.getNeededOffset(pageSize, pageNumber);

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {

            if (PageValidator.isSizeAndNumberAndRowNumberValid(pageSize, pageNumber, allRows, maxPageNumber)) {
                statement.setLong(1, pageSize);
                statement.setLong(2, neededOffset);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    resultProductList.add(Product.builder()
                            .id(resultSet.getLong("id"))
                            .title(resultSet.getString("title"))
                            .price(resultSet.getDouble("price"))
                            .discount(resultSet.getBoolean("discount")).build());
                }
            } else {
                throw new RuntimeException("The table is empty or Wrong page number");
            }
            return resultProductList;
        }
    }

    @Override
    public Optional<Product> findById(long id) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = handleProductResultSet(resultSet);
            return Optional.ofNullable(product);
        }
    }

    @Override
    public Optional<Product> findByName(String name) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            Product product = handleProductResultSet(resultSet);
            return Optional.ofNullable(product);
        }
    }

    @Override
    public long countAllRows() throws SQLException {
        long rows = 0;
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ROWS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rows = resultSet.getInt(1);
            }
        }
        return rows;
    }

    private Product handleProductResultSet(ResultSet resultSet) throws SQLException {
        Product result = null;
        while (resultSet.next()) {
            if (resultSet.getLong("id") != 0) {
                result = Product.builder()
                        .id(resultSet.getLong("id"))
                        .title(resultSet.getString("title"))
                        .price(resultSet.getDouble("price"))
                        .discount(resultSet.getBoolean("discount")).build();
            }
        }
        return result;
    }

    @Override
    public Product save(Product entity) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_NEW_ENTITY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getTitle());
            statement.setDouble(2, entity.getPrice());
            statement.setBoolean(3, entity.isDiscount());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                entity.setId(resultSet.getLong("id"));
            }
        }
        return entity;
    }

    @Override
    public boolean update(Product entity) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ENTITY)) {
            statement.setString(1, entity.getTitle());
            statement.setDouble(2, entity.getPrice());
            statement.setBoolean(3, entity.isDiscount());
            statement.setLong(4, entity.getId());
            return statement.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteById(long id) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        }
    }
}
