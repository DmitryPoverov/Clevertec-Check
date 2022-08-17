package ru.clevertec.flywayClasses;

import org.flywaydb.core.Flyway;

public class FlywayRunner1th2th3thScripts {

    private static final String URL = "jdbc:postgresql://localhost:5432/flyway_test";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String CLASSPATH = "classpath:/db/migration1";

    public static void main(String[] args) {

/*To create history table and apply 3 scripts: create scheme, table, fill table*/
        Flyway flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .locations(CLASSPATH)
                .load();
        flyway.migrate();
    }
}