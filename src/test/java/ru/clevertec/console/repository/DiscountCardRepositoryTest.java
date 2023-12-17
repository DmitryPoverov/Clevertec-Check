package ru.clevertec.console.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.console.config.AppConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class DiscountCardRepositoryTest {

    @Autowired
    DiscountCardRepository repository;

    @Test
    void testShouldReturnNumbersOfDiscountCards() {
        long countActual = repository.count();
        long countExpected = 5;
        Assertions.assertEquals(countActual, countExpected);
    }
}
