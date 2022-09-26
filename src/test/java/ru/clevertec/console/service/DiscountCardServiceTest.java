package ru.clevertec.console.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.console.config.AppConfig;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.service.interfaces.DiscountCardService;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class DiscountCardServiceTest {

    @Autowired
    DiscountCardService service;

    private final List<DiscountCard> expectedCards = List.of(
            DiscountCard.builder().id(1).number("card-120").build(),
            DiscountCard.builder().id(2).number("card-121").build(),
            DiscountCard.builder().id(3).number("card-122").build(),
            DiscountCard.builder().id(4).number("card-123").build(),
            DiscountCard.builder().id(5).number("card-777").build());

    @Test
    void testShouldReturnAll() {
        List<DiscountCard> actualCards = service.findAll();
        Assertions.assertEquals(expectedCards, actualCards);
    }

    @Test
    @Transactional
    @Rollback
    @Disabled
    void testShouldSaveDiscountCardAndDontCommitIt() {
        DiscountCard expectedCard = DiscountCard.builder().number("test-111").build();
        DiscountCard save = service.save(expectedCard);
        long id = save.getId();
        Optional<DiscountCard> byId = service.findById(id);
        if (byId.isPresent()) {
            DiscountCard actualCard = byId.get();
            Assertions.assertEquals(expectedCard, actualCard);
        }
    }


}
