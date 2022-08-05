package ru.clevertec.console;

import org.flywaydb.core.Flyway;

public class FlywayRunner {

    private static final String URL = "jdbc:postgresql://localhost:5432/flyway_test";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static void main(String[] args) {

/*To create history table and apply 3 scripts: create scheme, table, fill table
        Flyway flyway = Flyway.configure()
                .cleanDisabled(false)
                .dataSource(URL, USER, PASSWORD)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();*/

/*To delete schema and then to clean history*/
        Flyway flyway2 = Flyway.configure()
                .cleanDisabled(false)
                .ignoreMigrationPatterns("*:pending")
                .dataSource(URL, USER, PASSWORD)
                .locations("classpath:/db/migration2")
                .load();
        flyway2.repair();
        flyway2.migrate();
        flyway2.clean();
    }
}