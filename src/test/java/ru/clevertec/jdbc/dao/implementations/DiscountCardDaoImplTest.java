package ru.clevertec.jdbc.dao.implementations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.console.dao.daoInterface.DiscountCardDao;
import ru.clevertec.console.dao.implementations.DiscountCardDaoImpl;
import ru.clevertec.console.entities.DiscountCard;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DiscountCardDaoImplTest {

    private static final List<DiscountCard> EXPECTED_FULL_LIST = Arrays.asList(
            DiscountCard.builder().id(1).number("card-120").build(),
            DiscountCard.builder().id(2).number("card-121").build(),
            DiscountCard.builder().id(3).number("card-122").build(),
            DiscountCard.builder().id(4).number("card-123").build(),
            DiscountCard.builder().id(5).number("card-777").build());
    private static final List<DiscountCard> EXPECTED_LIST_2_ELEMENTS = Arrays.asList(
            DiscountCard.builder().id(1).number("card-120").build(),
            DiscountCard.builder().id(2).number("card-121").build());
    private static final DiscountCard EXPECTED_CORRECT_CARD = DiscountCard.builder().id(2).number("card-121").build();
    private static final DiscountCardDao<Integer, DiscountCard> DAO = DiscountCardDaoImpl.getInstance();
    private static final DiscountCard DISCOUNT_CARD = DiscountCard.builder().number("card-444").build();
    private static final int ZERO = 0;
    private static final int PAGE_NUMBER_FOR_FULL_LIST = 1;
    private static final int CORRECT_ID = 2;
    private static final int INCORRECT_CARD_ID = 100;
    private static final int ROW_TO_CHANGE = 5;
    private static final int ROW_TO_DELETE = 200;
    private static final String CORRECT_NAME = "card-121";

    @Test
    void testShouldAddNewEntityAndReturnItAndThenDeleteIt() throws SQLException {
        DiscountCard actual = DAO.save(DISCOUNT_CARD);
        Assertions.assertEquals(DISCOUNT_CARD, actual);
        boolean deleteById = DAO.deleteById(actual.getId());
        Assertions.assertTrue(deleteById);
    }

    @Test
    void testShouldReturnCorrectCardByName() throws SQLException {
        Optional<DiscountCard> byName = DAO.findByName(CORRECT_NAME);
        if (byName.isPresent()) {
            DiscountCard actualDiscountCard = byName.get();
            Assertions.assertEquals(actualDiscountCard, EXPECTED_CORRECT_CARD);
        }
    }

    @Test
    void testShouldUpdateLine() throws SQLException {
        boolean update = DAO.update(DiscountCard.builder().id(ROW_TO_CHANGE).number("card-777").build());
        Assertions.assertTrue(update);
    }

    //The test checks: when only 2 elements are returning (limit 2, offset 1 - are default values)
    @Test
    void testShouldReturn2ElementsArrayListOfDiscountCard() throws SQLException {
        List<DiscountCard> actual = DAO.findAll(ZERO, ZERO);
        Assertions.assertEquals(EXPECTED_LIST_2_ELEMENTS, actual);
    }

    //The test checks: when all elements are returning
    @Test
    void testShouldReturnAllElementsArrayListOfDiscountCard() throws SQLException {
        List<DiscountCard> actual = DAO.findAll(DAO.countAllRows(), PAGE_NUMBER_FOR_FULL_LIST);
        Assertions.assertEquals(EXPECTED_FULL_LIST, actual);
    }

    @Test
    void testShouldNotDeleteOneRow() throws SQLException {
        boolean deleteById = DAO.deleteById(ROW_TO_DELETE);
        Assertions.assertFalse(deleteById);
    }

    @Test
    void testShouldReturnCorrectCard() throws SQLException {
        Optional<DiscountCard> byId = DAO.findById(CORRECT_ID);
        if (byId.isPresent()) {
            DiscountCard actual = byId.get();
            Assertions.assertEquals(EXPECTED_CORRECT_CARD, actual);
        }
    }

    @Test
    void testShouldReturnIncorrectCard() throws SQLException {
        Optional<DiscountCard> byId = DAO.findById(INCORRECT_CARD_ID);
        Assertions.assertThrows(NoSuchElementException.class, byId::get);
    }
}
