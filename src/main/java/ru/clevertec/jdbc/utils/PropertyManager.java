package ru.clevertec.jdbc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

    private static final String FILE = "db.properties";
    private static final String URL = "db.url";
    private static final String USER = "db.user";
    private static final String PASSWORD = "db.password";
    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        loadProperties();
    }

    private PropertyManager() {
    }

    private static void loadProperties() {
        try(InputStream inputStream =
                    PropertyManager.class.getClassLoader().getResourceAsStream(FILE)) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getUrl() {
        return PROPERTIES.getProperty(URL);
    }

    public static String getUser() {
        return PROPERTIES.getProperty(USER);
    }

    public static String getPassword() {
        return PROPERTIES.getProperty(PASSWORD);
    }
}
