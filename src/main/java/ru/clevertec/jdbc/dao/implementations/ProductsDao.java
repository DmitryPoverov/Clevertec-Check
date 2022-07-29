package ru.clevertec.jdbc.dao.implementations;

import ru.clevertec.jdbc.dao.daoInterface.Dao;
import ru.clevertec.jdbc.entities.Product;
import ru.clevertec.jdbc.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsDao implements Dao<Integer, Product> {

    private static final ProductsDao INSTANCE = new ProductsDao();
    private static final String FIND_ALL = """
            SELECT id, title, price, discount
            FROM check_products
            ORDER BY id
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

    private ProductsDao() {
    }

    public static Dao<Integer, Product> getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Product> result = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setTitle(resultSet.getString("title"));
                product.setPrice(resultSet.getDouble("price"));
                product.setDiscount(resultSet.getBoolean("discount"));
                result.add(product);
            }
            return result;
        }
    }

    @Override
    public Optional<Product> findById(Integer id) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = new Product();
            handleResultSet(product, resultSet);
            return Optional.of(product);
        }
    }

    @Override
    public Optional<Product> findByName(String name) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            Product product = new Product();
            handleResultSet(product, resultSet);
            return Optional.of(product);
        }
    }

    private void handleResultSet(Product product, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            product.setId(resultSet.getInt("id"));
            product.setTitle(resultSet.getString("title"));
            product.setPrice(resultSet.getDouble("price"));
            product.setDiscount(resultSet.getBoolean("discount"));
        }
    }

    @Override
    public Product save(Product entity) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_NEW_ENTITY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getTitle());
            statement.setDouble(2, entity.getPrice());
            statement.setBoolean(3, entity.isDiscount());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                entity.setId(resultSet.getInt("id"));
            }
        }
        return entity;
    }

    @Override
    public boolean update(Product entity) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ENTITY)) {
            statement.setString(1, entity.getTitle());
            statement.setDouble(2, entity.getPrice());
            statement.setBoolean(3, entity.isDiscount());
            statement.setInt(4, entity.getId());
            return statement.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }
}
