package ru.clevertec.jdbc.dao.implementations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.jdbc.dao.daoInterface.Dao;
import ru.clevertec.jdbc.entities.Product;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductsDaoTest {

    private static final List<Product> EXPECTED_LIST = Arrays.asList(
            new Product( 1,"Dress1",10.11,false),
            new Product(2,"Pants1",10.22,false),
            new Product(3,"Boots1",25.33,true),
            new Product(4,"Shoes1",30.44,true),
            new Product(5,"Jacket1",35.55,true),
            new Product(6,"Hat1",140.66,true),
            new Product(7,"Hat2",40.77,true),
            new Product(8,"West1",45.88,false),
            new Product(9,"West2",45.99,true),
            new Product(10,"Dress2",15.00,true),
            new Product(11,"Pants2",20.11,true),
            new Product(12,"Boots2",25.22,false),
            new Product(13,"Shoes2",30.33,true),
            new Product(14,"Jacket2",35.44,true)
    );
    private static final Dao<Integer, Product> DAO = ProductsDao.getInstance();
    private static final int CORRECT_ID = 1;
    private static final Product EXPECTED_PRODUCT = new Product( 1,"Dress1",10.11,false);
    private static final int INCORRECT_ID = 111;
    private static final Product EXPECTED_INCORRECT_PRODUCT = new Product();
    private static final Product EXPECTED_SAVED_PRODUCT = new Product("TEST",11.99,true);
    private static final Product EXPECTED_UPDATED_PRODUCT = new Product(2,"Pants1",10.22,false);
    private static final int UPDATED_ID = 2;
    private static final int TRUE_ID = 3;
    private static final int ID_TO_DELETE = 31;
    private static final String CORRECT_ID_NAME = "Dress1";

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

    @Test
    void testShouldReturnListOfGoodsFromDB() throws SQLException {
        List<Product> actual = DAO.findAll();
        Assertions.assertEquals(EXPECTED_LIST, actual);
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
