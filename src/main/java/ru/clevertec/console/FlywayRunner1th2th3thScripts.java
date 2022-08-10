package ru.clevertec.console;

import org.flywaydb.core.Flyway;

public class FlywayRunner1th2th3thScripts {

    private static final String URL = "jdbc:postgresql://localhost:5432/flyway_test";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static void main(String[] args) {

/*To create history table and apply 3 scripts: create scheme, table, fill table*/
        Flyway flyway = Flyway.configure()
                .cleanDisabled(false)
                .dataSource(URL, USER, PASSWORD)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
    }
}