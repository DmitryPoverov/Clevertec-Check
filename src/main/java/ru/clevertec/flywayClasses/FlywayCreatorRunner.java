package ru.clevertec.flywayClasses;

public class FlywayCreatorRunner {

    public static void main(String[] args) {

        FlywayCreator.createAndFill();
        FlywayCreator.dropEverything();
    }
}
