package ru.clevertec.console.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String DB_URL = PropertyManager.getUrl();
    private static final String DB_USER = PropertyManager.getUser();
    private static final String DB_PASSWORD = PropertyManager.getPassword();

    //TODO private constructor?

    public static Connection getConnection() throws SQLException {
        Connection connection;
        ProxyConnection proxyConnection;

        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        proxyConnection = new ProxyConnection(connection);

        return proxyConnection;
    }
}