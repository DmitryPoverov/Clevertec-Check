package ru.clevertec.console.dao.implementations;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.clevertec.console.dao.daoInterface.ProductDao;
import ru.clevertec.console.entities.Product;
import ru.clevertec.flywayClasses.FlywayCreator;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductDaoImplTest {

    private static final List<Product> EXPECTED_FULL_LIST = Arrays.asList(
            Product.builder().id(1).title("Dress1_db").price(10.11).discount(false).build(),
            Product.builder().id(2).title("Pants1_db").price(10.22).discount(false).build(),
            Product.builder().id(3).title("Boots1_db").price(25.33).discount(true).build(),
            Product.builder().id(4).title("Shoes1_db").price(30.44).discount(true).build(),
            Product.builder().id(5).title("Jacket1_db").price(35.55).discount(true).build(),
            Product.builder().id(6).title("Hat1_db").price(140.66).discount(true).build(),
            Product.builder().id(7).title("Hat2_db").price(40.77).discount(true).build(),
            Product.builder().id(8).title("West1_db").price(45.88).discount(false).build(),
            Product.builder().id(9).title("West2_db").price(45.99).discount(true).build(),
            Product.builder().id(10).title("Dress2_db").price(15.00).discount(true).build(),
            Product.builder().id(11).title("Pants2_db").price(20.11).discount(true).build(),
            Product.builder().id(12).title("Boots2_db").price(25.22).discount(false).build(),
            Product.builder().id(13).title("Shoes2_db").price(30.33).discount(true).build(),
            Product.builder().id(14).title("Jacket2_db").price(35.44).discount(true).build());

    private static final List<Product> EXPECTED_LIST_OF_3_ELEMENTS = Arrays.asList(
            Product.builder().id(1).title("Dress1_db").price(10.11).discount(false).build(),
            Product.builder().id(2).title("Pants1_db").price(10.22).discount(false).build(),
            Product.builder().id(3).title("Boots1_db").price(25.33).discount(true).build());
    private static final ProductDao DAO = new ProductDaoImpl();
    private static final int CORRECT_ID = 1;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final Product EXPECTED_PRODUCT = Product.builder().id(1).title("Dress1_db").price(10.11).discount(false).build();
    private static final int INCORRECT_ID = 111;
    private static final Product EXPECTED_INCORRECT_PRODUCT = Product.builder().build();
    private static final Product EXPECTED_SAVED_PRODUCT = Product.builder().id(1).title("TEST").price(11.99).discount(true).build();
    private static final Product EXPECTED_UPDATED_PRODUCT = Product.builder().id(2).title("Pants1_db").price(10.22).discount(false).build();
    private static final int UPDATED_ID = 2;
    private static final int TRUE_ID = 3;
    private static final int ID_TO_DELETE = 31;
    private static final double EXPECTED_PRICE_BY_ID = 10.11;
    private static final String CORRECT_ID_NAME = "Dress1_db";

    @BeforeAll
    static void init() {
        FlywayCreator.createAndFill();
    }

    @AfterAll
    static void close() {
        FlywayCreator.dropEverything();
    }

    @Test
    void testShouldReturnPriceById() throws SQLException {
        if (DAO.findById(CORRECT_ID).isPresent()) {
            double priceById = DAO.findById(CORRECT_ID).get().getPrice();
            Assertions.assertEquals(EXPECTED_PRICE_BY_ID, priceById);
        }
    }

    @Test
    void testShouldReturnProductByName() throws SQLException {
        Optional<Product> actualProduct = DAO.findByName(CORRECT_ID_NAME);
        actualProduct.ifPresent(product -> Assertions.assertEquals(EXPECTED_PRODUCT, product));
    }

    @Test
    void testShouldReturnFalse() throws SQLException {
        if (DAO.findById(UPDATED_ID).isPresent()) {
            boolean discountById = DAO.findById(UPDATED_ID).get().isDiscount();
            Assertions.assertFalse(discountById);
        }
    }

    @Test
    void testShouldReturnTrue() throws SQLException {
        if (DAO.findById(TRUE_ID).isPresent()) {
            boolean discountById = DAO.findById(TRUE_ID).get().isDiscount();
            Assertions.assertTrue(discountById);
        }
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
