package ru.clevertec.console.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String DB_URL = PropertyManager.getUrl();
    private static final String DB_USER = PropertyManager.getUser();
    private static final String DB_PASSWORD = PropertyManager.getPassword();
    private static final String DB_DRIVER = PropertyManager.getDriver();

    static {
        loadDriver();
    }

    private ConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {

        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ProxyConnection(connection);
    }

    private static void loadDriver(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}