package ru.clevertec.console;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardsTest {

    Stream<String> generateCorrectCardNumbers() {
        return Stream.of("card-120", "card-121", "card-122", "card-123");
    }

    @ParameterizedTest
    @MethodSource("generateCorrectCardNumbers")
    void testShouldConfirmCardNumber(String number) {
        boolean isExist = Cards.isSuchCard(number);
        Assertions.assertTrue(isExist);
    }

    @ParameterizedTest
    @ValueSource(strings = {"card-124", "card-125", "card-126", "card-127"})
    void testShouldDoNotConfirmCardNumber(String number) {
        boolean isExist = Cards.isSuchCard(number);
        Assertions.assertFalse(isExist);
    }


}
