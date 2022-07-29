package ru.clevertec.jdbc.dao.implementations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.jdbc.dao.daoInterface.Dao;
import ru.clevertec.jdbc.entities.DiscountCard;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DiscountCardDaoTest {

    private static final List<DiscountCard> CORRECT_LIST = Arrays.asList(
            new DiscountCard(1, 120),
            new DiscountCard(2, 121),
            new DiscountCard(3, 122),
            new DiscountCard(4, 123),
            new DiscountCard(5, 777));
    private static final DiscountCard CORRECT_CARD = new DiscountCard(2, 121);
    private static final DiscountCard INCORRECT_CARD = new DiscountCard();
    private static final int CORRECT_ID = 2;
    private static final int INCORRECT_ID = 10;
    private static final int ROW_TO_CHANGE = 5;
    private static final int ROW_TO_DELETE = 200;
    private static final Dao<Integer, DiscountCard> DAO = DiscountCardDao.getInstance();
    private static final DiscountCard DISCOUNT_CARD = new DiscountCard(444);

    @Test
    void testShouldAddNewEntityAndReturnItAndThenDeleteIt() throws SQLException {
        DiscountCard actual = DAO.save(DISCOUNT_CARD);
        Assertions.assertEquals(DISCOUNT_CARD, actual);
        boolean deleteById = DAO.deleteById(actual.getId());
        Assertions.assertTrue(deleteById);
    }

    @Test
    void testShouldUpdateLine() throws SQLException {
        boolean update = DAO.update(new DiscountCard(ROW_TO_CHANGE, 777));
        Assertions.assertTrue(update);
    }

    @Test
    void testShouldReturnArrayListOfDiscountCard() throws SQLException {
        List<DiscountCard> actual = DAO.findAll();
        Assertions.assertEquals(CORRECT_LIST, actual);
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
            Assertions.assertEquals(CORRECT_CARD, actual);
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
