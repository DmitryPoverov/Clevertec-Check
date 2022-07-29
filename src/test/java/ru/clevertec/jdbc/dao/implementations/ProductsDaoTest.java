package ru.clevertec.jdbc.dao.implementations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.jdbc.dao.daoInterface.Dao;
import ru.clevertec.jdbc.dao.implementations.ProductsDao;
import ru.clevertec.jdbc.entities.Product;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductsDaoTest {

    private static final List<Product> EXPECTED_LIST = Arrays.asList(
            new Product( 1,"Dress1",10.01,false),
            new Product(2,"Pants1",10.01,false),
            new Product(3,"Boots1",25.00,true),
            new Product(4,"Shoes1",30.00,true),
            new Product(5,"Jacket1",35.00,true),
            new Product(6,"Hat1",140.00,true),
            new Product(7,"Hat2",40.00,true),
            new Product(8,"West1",45.00,false),
            new Product(9,"West2",45.00,true),
            new Product(10,"Dress2",15.00,true),
            new Product(11,"Pants2",20.00,true),
            new Product(12,"Boots2",25.00,false),
            new Product(13,"Shoes2",30.00,true),
            new Product(14,"Jacket2",35.00,true)
    );
    private static final Dao<Integer, Product> INSTANCE = ProductsDao.getInstance();
    private static final int CORRECT_ID = 1;
    private static final Product EXPECTED_PRODUCT = new Product( 1,"Dress1",10.01,false);
    private static final int INCORRECT_ID = 111;
    private static final Product EXPECTED_INCORRECT_PRODUCT = new Product();
    private static final Product EXPECTED_SAVED_PRODUCT = new Product("TEST",11.99,true);
    private static final Product EXPECTED_UPDATED_PRODUCT = new Product(2,"Pants1",10.01,false);
    private static final int UPDATED_ID = 2;
    private static final int ID_TO_DELETE = 31;

    @Test
    void testShouldReturnListOfGoodsFromDB() throws SQLException {
        List<Product> actual = INSTANCE.findAll();
        Assertions.assertEquals(EXPECTED_LIST, actual);
    }

    @Test
    void testShouldReturnCorrectProductById() throws SQLException {
        Optional<Product> byId = INSTANCE.findById(CORRECT_ID);
        if (byId.isPresent()) {
            Product actual = byId.get();
            Assertions.assertEquals(EXPECTED_PRODUCT, actual);
        }
    }

    @Test
    void testShouldReturnInCorrectProductById() throws SQLException {
        Optional<Product> byId = INSTANCE.findById(INCORRECT_ID);
        if (byId.isPresent()) {
            Product actual = byId.get();
            Assertions.assertEquals(EXPECTED_INCORRECT_PRODUCT, actual);
        }
    }

    @Test
    void testShouldUpdateProductAndReturnTrue() throws SQLException{
        boolean update = INSTANCE.update(EXPECTED_UPDATED_PRODUCT);
        Assertions.assertTrue(update);
        Optional<Product> byId = INSTANCE.findById(UPDATED_ID);
        byId.ifPresent(product -> Assertions.assertEquals(EXPECTED_UPDATED_PRODUCT, product));
    }

    @Test
    void testShouldDeleteTestProduct() throws SQLException {
        boolean deleteById = INSTANCE.deleteById(ID_TO_DELETE);
        Assertions.assertFalse(deleteById);
    }

    @Test
//    @Disabled
    void testShouldSaveAndReturnNewProductAndThenDeleteIt() throws SQLException {
        Product actualSavedProduct = INSTANCE.save(EXPECTED_SAVED_PRODUCT);
        Optional<Product> byId = INSTANCE.findById(actualSavedProduct.getId());
        byId.ifPresent(product -> Assertions.assertEquals(EXPECTED_SAVED_PRODUCT, product));
        INSTANCE.deleteById(actualSavedProduct.getId());
    }
}
