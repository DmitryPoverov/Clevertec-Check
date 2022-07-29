package ru.clevertec.console;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.exception.WrongIdException;
import org.junit.Assert;
import org.junit.Test;

public class ProductsTest {

    private static final int CORRECT_ID = 10;
    private static final int WRONG_ID = 100;

    @Test
    public void testPrintToConsoleShouldReturnId() {
        //given
        String expected = "Dress2";
        //when
        String actual = Products.getDescriptionById(CORRECT_ID);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetDescriptionByIdShouldThrowsException() {
        //given
        //when
        Assertions.assertThrows(WrongIdException.class, () -> Products.getDescriptionById(WRONG_ID));
        //then
    }

    @Test
    void testGetPriceByIdShouldReturnPrice() {
        //given
        double expected = 15;
        //when
        double actual = Products.getPriceById(CORRECT_ID);
        //then
        Assertions.assertEquals(expected, actual, 0);
    }

    @Test
    void testGetPriceByIdShouldThrowsException() {
        Assertions.assertThrows(WrongIdException.class, () -> Products.getPriceById(WRONG_ID));
    }

    @Test
    void testIsDiscountShouldReturnTrue() {
        Assertions.assertTrue(Products.isDiscount(CORRECT_ID));
    }
    @Test
    void testIsDiscountShouldReturnFalse() {
        Assertions.assertTrue(Products.isDiscount(CORRECT_ID));
    }

    @Test(expected = WrongIdException.class)
    public void testGetPriceByIdShouldThrowsException() {
        //given
        //when
        Products.getPriceById(WRONG_ID);
        //then
    }
}
