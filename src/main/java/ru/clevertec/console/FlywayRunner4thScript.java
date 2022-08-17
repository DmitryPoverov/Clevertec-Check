package ru.clevertec.console;

import org.flywaydb.core.Flyway;

public class FlywayRunner4thScript {

    private static final String URL = "jdbc:postgresql://localhost:5432/flyway_test";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static void main(String[] args) {

/*To delete schema and then to clean history*/
        Flyway flyway = Flyway.configure()
                .cleanDisabled(false)
                .ignoreMigrationPatterns("*:pending")
                .dataSource(URL, USER, PASSWORD)
                .locations("classpath:/db/migration2")
                .load();
        flyway.repair();
        flyway.migrate();
        flyway.clean();
    }
}