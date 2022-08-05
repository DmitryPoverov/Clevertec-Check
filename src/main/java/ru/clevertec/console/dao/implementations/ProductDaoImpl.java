package ru.clevertec.console.dao.implementations;

import ru.clevertec.console.dao.daoInterface.ProductDao;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.utils.ConnectionManager;
import ru.clevertec.console.utils.ProxyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao<Integer, Product> {

    private static final ProductDao<Integer, Product> INSTANCE = new ProductDaoImpl();
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

    private ProductDaoImpl() {}

    public static ProductDao<Integer, Product> getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Product> findAll(Integer pageSize, Integer pageNumber) throws SQLException {

        ArrayList<Product> cardList = new ArrayList<>();
        int rows = countAllRows();
        double maxPageNumber = 0.0;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {

            if (pageSize <= 0) {
                pageSize = 3;
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
                statement.setInt(1, pageSize);
                statement.setInt(2, pageSize * pageNumber - pageSize);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setTitle(resultSet.getString("title"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setDiscount(resultSet.getBoolean("discount"));
                    cardList.add(product);
                }
            }
            return cardList;
        }
    }

    @Override
    public Optional<Product> findById(Integer id) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = new Product();
            handleResultSet(product, resultSet);
            return Optional.of(product);
        }
    }

    @Override
    public Integer countAllRows() throws SQLException {
        int rows = 0;
        try (Connection connection = new ProxyConnection(ConnectionManager.getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ROWS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rows = resultSet.getInt(1);
            }
        }
        return rows;
    }

    @Override
    public String getNameById(Integer id) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = new Product();
            handleResultSet(product, resultSet);
            return product.getTitle();
        }
    }

    @Override
    public double getPriceById(Integer id) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = new Product();
            handleResultSet(product, resultSet);
            return product.getPrice();
        }
    }

    @Override
    public boolean isDiscountById(Integer id) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = new Product();
            handleResultSet(product, resultSet);
            return product.isDiscount();
        }
    }

    @Override
    public Optional<Product> findByName(String name) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
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
        try (Connection connection = ConnectionManager.getConnection();
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
        try (Connection connection = ConnectionManager.getConnection();
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
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }
}
