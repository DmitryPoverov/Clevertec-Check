package ru.clevertec.flywayClasses;

import org.flywaydb.core.Flyway;

public class FlywayRunner4thScript {

    private static final String URL = "jdbc:postgresql://localhost:5432/flyway_test";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String CLASSPATH = "classpath:/db/migration2";

    public static void main(String[] args) {

/*To delete schema and then to clean history*/
        Flyway flyway = Flyway.configure()
                .cleanDisabled(false)
                .ignoreMigrationPatterns("*:pending")
                .dataSource(URL, USER, PASSWORD)
                .locations(CLASSPATH)
                .load();
        flyway.repair();
        flyway.migrate();
        flyway.clean();
    }
}