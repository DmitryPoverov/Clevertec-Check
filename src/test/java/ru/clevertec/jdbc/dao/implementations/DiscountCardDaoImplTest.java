package ru.clevertec.jdbc.dao.implementations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.console.dao.daoInterface.DiscountCardDao;
import ru.clevertec.console.dao.implementations.DiscountCardDaoImpl;
import ru.clevertec.console.entities.DiscountCard;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DiscountCardDaoImplTest {

    private static final List<DiscountCard> EXPECTED_FULL_LIST = Arrays.asList(
            new DiscountCard(1, "card-120"),
            new DiscountCard(2, "card-121"),
            new DiscountCard(3, "card-122"),
            new DiscountCard(4, "card-123"),
            new DiscountCard(5, "card-777"));
    private static final List<DiscountCard> EXPECTED_LIST_2_ELEMENTS = Arrays.asList(
            new DiscountCard(1, "card-120"),
            new DiscountCard(2, "card-121"));
    private static final int ZERO = 0;
    private static final int PAGE_NUMBER_FOR_FULL_LIST = 1;
    private static final DiscountCard EXPECTED_CARD = new DiscountCard(2, "card-121");
    private static final DiscountCard INCORRECT_CARD = new DiscountCard();
    private static final int CORRECT_ID = 2;
    private static final int INCORRECT_ID = 10;
    private static final int ROW_TO_CHANGE = 5;
    private static final int ROW_TO_DELETE = 200;
    private static final DiscountCardDao<Integer, DiscountCard> DAO = DiscountCardDaoImpl.getInstance();
    private static final DiscountCard DISCOUNT_CARD = new DiscountCard("card-444");

    @Test
    void testShouldAddNewEntityAndReturnItAndThenDeleteIt() throws SQLException {
        DiscountCard actual = DAO.save(DISCOUNT_CARD);
        Assertions.assertEquals(DISCOUNT_CARD, actual);
        boolean deleteById = DAO.deleteById(actual.getId());
        Assertions.assertTrue(deleteById);
    }

    @Test
    void testShouldUpdateLine() throws SQLException {
        boolean update = DAO.update(new DiscountCard(ROW_TO_CHANGE, "card-777"));
        Assertions.assertTrue(update);
    }

    //The test checks: when only 3 elements are returning (limit 2, offset 1 - are default values)
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
            Assertions.assertEquals(EXPECTED_CARD, actual);
        }
    }

    @Test
    void testShouldReturnIncorrectCard() throws SQLException {
        Optional<DiscountCard> byId = DAO.findById(INCORRECT_ID);
        if (byId.isPresent()) {
            DiscountCard actual = byId.get();
            Assertions.assertEquals(INCORRECT_CARD, actual);
        }
    }
}
