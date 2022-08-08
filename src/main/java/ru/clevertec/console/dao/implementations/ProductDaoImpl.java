package ru.clevertec.console.dao.implementations;

import ru.clevertec.console.dao.daoInterface.ProductDao;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.serviceClass.CheckService;
import ru.clevertec.console.serviceClass.CheckServiceImpl;
import ru.clevertec.console.utils.ConnectionManager;
import ru.clevertec.console.utils.ProxyConnection;
import ru.clevertec.console.validators.PageValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao<Integer, Product> {

    private static final ProductDao<Integer, Product> PRODUCT_DAO = new ProductDaoImpl();
    private static final CheckService SERVICE = CheckServiceImpl.getInstance();
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

    private ProductDaoImpl() {
    }

    public static ProductDao<Integer, Product> getInstance() {
        return PRODUCT_DAO;
    }

    @Override
    public List<Product> findAll(Integer pageSize, Integer pageNumber) throws SQLException {

        ArrayList<Product> resultProductList = new ArrayList<>();
        int allRows = countAllRows();
        double maxPageNumber = 0.0;
        int neededOffset;

        pageSize = PageValidator.checkAndReturnProductPageSize(pageSize);
        pageNumber = PageValidator.checkAndReturnPageNumber(pageNumber);
        maxPageNumber = PageValidator.checkAndReturnMaxPageNumber(pageSize, allRows, maxPageNumber);
        neededOffset = SERVICE.getNeededOffset(pageSize, pageNumber);

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {

            if (PageValidator.isSizeAndNumberAndRowNumberValid(pageSize, pageNumber, allRows, maxPageNumber)) {
                statement.setInt(1, pageSize);
                statement.setInt(2, neededOffset);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    resultProductList.add(Product.builder()
                            .id(resultSet.getInt("id"))
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
    public Optional<Product> findById(Integer id) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
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
            Product product = Product.builder().build();
            handleProductResultSet(product, resultSet);
            return product.getTitle();
        }
    }

    @Override
    public double getPriceById(Integer id) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = Product.builder().build();
            handleProductResultSet(product, resultSet);
            return product.getPrice();
        }
    }

    @Override
    public boolean isDiscountById(Integer id) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = Product.builder().build();       // <- This one is like in my example
            handleProductResultSet(product, resultSet);
            return product.isDiscount();
        }
    }

    private void handleProductResultSet(Product product, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            product.setId(resultSet.getInt("id"));
            product.setTitle(resultSet.getString("title"));
            product.setPrice(resultSet.getDouble("price"));
            product.setDiscount(resultSet.getBoolean("discount"));
        }
    }

    private Product handleProductResultSet(ResultSet resultSet) throws SQLException {
        Product result = null;
        while (resultSet.next()) {
            if (resultSet.getInt("id") != 0) {
                result = Product.builder()
                        .id(resultSet.getInt("id"))
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
