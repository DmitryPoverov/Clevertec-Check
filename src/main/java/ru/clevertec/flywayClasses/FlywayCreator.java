package ru.clevertec.flywayClasses;

import lombok.experimental.UtilityClass;
import org.flywaydb.core.Flyway;

@UtilityClass
public class FlywayCreator {

    private static final String URL = "jdbc:postgresql://localhost:5432/flyway_test";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String CLASSPATH1 = "classpath:/db/migration1";
    private static final String CLASSPATH2 = "classpath:/db/migration2";


    public static void createAndFill() {
        Flyway flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .locations(CLASSPATH1)
                .load();
        flyway.migrate();
    }

    public static void dropEverything() {
        Flyway flyway = Flyway.configure()
                .cleanDisabled(false)
                .ignoreMigrationPatterns("*:pending")
                .dataSource(URL, USER, PASSWORD)
                .locations(CLASSPATH2)
                .load();
        flyway.repair();
        flyway.migrate();
        flyway.clean();
    }
}
