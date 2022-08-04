package ru.clevertec.console;

import org.flywaydb.core.Flyway;

public class FlywayRunner {

    private static final String URL = "jdbc:postgresql://localhost:5432/flyway_test";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static void main(String[] args) {

        Flyway flyway = Flyway.configure()
                .cleanDisabled(false)
                .dataSource(URL, USER, PASSWORD)
                .locations("classpath:/db/migration2")
                .load();

//Flyway state checking
//        Configuration configuration = flyway.getConfiguration();
//        System.out.println(configuration.isCleanDisabled());

//TO apply 3 scripts from migration2
        flyway.migrate();

//To clean all changes from 3 scripts which have been applied earlier
//        flyway.clean();

    }
}