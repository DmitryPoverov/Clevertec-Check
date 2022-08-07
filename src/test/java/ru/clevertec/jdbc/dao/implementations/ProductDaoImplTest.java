package ru.clevertec.jdbc.dao.implementations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.console.dao.daoInterface.ProductDao;
import ru.clevertec.console.dao.implementations.ProductDaoImpl;
import ru.clevertec.console.entities.Product;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductDaoImplTest {

    private static final List<Product> EXPECTED_FULL_LIST = Arrays.asList(
            new Product( 1,"Dress1_db",10.11,false),
            new Product(2,"Pants1_db",10.22,false),
            new Product(3,"Boots1_db",25.33,true),
            new Product(4,"Shoes1_db",30.44,true),
            new Product(5,"Jacket1_db",35.55,true),
            new Product(6,"Hat1_db",140.66,true),
            new Product(7,"Hat2_db",40.77,true),
            new Product(8,"West1_db",45.88,false),
            new Product(9,"West2_db",45.99,true),
            new Product(10,"Dress2_db",15.00,true),
            new Product(11,"Pants2_db",20.11,true),
            new Product(12,"Boots2_db",25.22,false),
            new Product(13,"Shoes2_db",30.33,true),
            new Product(14,"Jacket2_db",35.44,true));
    private static final List<Product> EXPECTED_LIST_OF_3_ELEMENTS = Arrays.asList(
            new Product( 1,"Dress1_db",10.11,false),
            new Product(2,"Pants1_db",10.22,false),
            new Product(3,"Boots1_db",25.33,true));
    private static final ProductDao<Integer, Product> DAO = ProductDaoImpl.getInstance();
    private static final int CORRECT_ID = 1;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final Product EXPECTED_PRODUCT = new Product( 1,"Dress1_db",10.11,false);
    private static final int INCORRECT_ID = 111;
    private static final Product EXPECTED_INCORRECT_PRODUCT = new Product();
    private static final Product EXPECTED_SAVED_PRODUCT = new Product("TEST",11.99,true);
    private static final Product EXPECTED_UPDATED_PRODUCT = new Product(2,"Pants1_db",10.22,false);
    private static final int UPDATED_ID = 2;
    private static final int TRUE_ID = 3;
    private static final int ID_TO_DELETE = 31;
    private static final double EXPECTED_PRICE_BY_ID = 10.11;
    private static final String CORRECT_ID_NAME = "Dress1_db";

    @Test
    void testShouldReturnPriceById() throws SQLException {
        double priceById = DAO.getPriceById(CORRECT_ID);
        Assertions.assertEquals(EXPECTED_PRICE_BY_ID, priceById);
    }

    @Test
    void testShouldReturnProductByName() throws SQLException {
        Optional<Product> actualProduct = DAO.findByName(CORRECT_ID_NAME);
        actualProduct.ifPresent(product -> Assertions.assertEquals(EXPECTED_PRODUCT, product));
    }

    @Test
    void testShouldReturnNameById() throws SQLException {
        String nameById = DAO.getNameById(CORRECT_ID);
        Assertions.assertEquals(CORRECT_ID_NAME, nameById);
    }

    @Test
    void testShouldReturnFalse() throws SQLException {
        boolean discountById = DAO.isDiscountById(UPDATED_ID);
        Assertions.assertFalse(discountById);
    }

    @Test
    void testShouldReturnTrue() throws SQLException {
        boolean discountById = DAO.isDiscountById(TRUE_ID);
        Assertions.assertTrue(discountById);
    }

    @Test //The test checks: when all elements are returning
    void testShouldReturn3ElementsListOfGoodsFromDB() throws SQLException {
        List<Product> actual = DAO.findAll(DAO.countAllRows(), ONE);
        Assertions.assertEquals(EXPECTED_FULL_LIST, actual);
    }

    @Test //The test checks: when only 3 elements are returning (limit 3, offset 1 - are default values)
    void testShouldReturnListOfGoodsFromDB() throws SQLException {
        List<Product> actual = DAO.findAll(ZERO, -10);
        Assertions.assertEquals(EXPECTED_LIST_OF_3_ELEMENTS, actual);
    }

    @Test
    void testShouldReturnCorrectProductById() throws SQLException {
        Optional<Product> byId = DAO.findById(CORRECT_ID);
        if (byId.isPresent()) {
            Product actual = byId.get();
            Assertions.assertEquals(EXPECTED_PRODUCT, actual);
        }
    }

    @Test
    void testShouldReturnInCorrectProductById() throws SQLException {
        Optional<Product> byId = DAO.findById(INCORRECT_ID);
        if (byId.isPresent()) {
            Product actual = byId.get();
            Assertions.assertEquals(EXPECTED_INCORRECT_PRODUCT, actual);
        }
    }

    @Test
    void testShouldUpdateProductAndReturnTrue() throws SQLException{
        boolean update = DAO.update(EXPECTED_UPDATED_PRODUCT);
        Assertions.assertTrue(update);
        Optional<Product> byId = DAO.findById(UPDATED_ID);
        byId.ifPresent(product -> Assertions.assertEquals(EXPECTED_UPDATED_PRODUCT, product));
    }

    @Test
    void testShouldDeleteTestProduct() throws SQLException {
        boolean deleteById = DAO.deleteById(ID_TO_DELETE);
        Assertions.assertFalse(deleteById);
    }

    @Test
//    @Disabled
    void testShouldSaveAndReturnNewProductAndThenDeleteIt() throws SQLException {
        Product actualSavedProduct = DAO.save(EXPECTED_SAVED_PRODUCT);
        Optional<Product> byId = DAO.findById(actualSavedProduct.getId());
        byId.ifPresent(product -> Assertions.assertEquals(EXPECTED_SAVED_PRODUCT, product));
        DAO.deleteById(actualSavedProduct.getId());
    }
}
