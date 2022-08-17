package ru.clevertec.console;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.clevertec.console.check.Check;
import ru.clevertec.console.entities.CheckItem;
import ru.clevertec.console.serviceClass.CheckServiceImpl;
import ru.clevertec.console.validators.RegexValidator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckTest {

    private static final String[] ARGS = new String[]{"1-2", "2-2", "card-123"};
    private static final Check CHECK_2 = new Check(ARGS);
    private static final String DISCOUNT_CARD_EXPECTED = "123";
    private static final CheckItem PM11 = new CheckItem(1, 2);
    private static final CheckItem PM12 = new CheckItem(2, 2);
    private static final CheckItem PM21 = new CheckItem(28, "Apple", 1.12, 2);
    private static final CheckItem PM22 = new CheckItem(30, "Watermelon", 2.45, 4);
    private static final CheckItem PM23 = new CheckItem(26, "Cherry", 3.18, 6);
    private static final CheckItem PM24 = new CheckItem(39, "Strawberry", 5.2, 8);
    private static final CheckItem PM25 = new CheckItem(35, "Nectarine", 3.17, 9);
    private static final List<CheckItem> LIST_PM_EXPECTED1 = Arrays.asList(PM11, PM12);
    private static final List<CheckItem> LIST_PM_EXPECTED2 = Arrays.asList(PM21, PM22, PM23, PM24, PM25);
    private static final String[] EXPECTED_CONTENT = {
            "28;Apple;1.12;2",
            "30;Watermelon;2.45;4",
            "8;Orange;0.99;5",
            "19;Pear;0.85;1",
            "26;Cherry;3.18;6",
            "39;Strawberry;5.20;8",
            "35;Nectarine;3.17;9",
            "110;Apple;1.12;2",
            "28;MyApple;1.12;2",
            "28;Apple;2.001;2",
            "28;Apple;1.12;50"};
    private static final String EXPECTED = """
            --------------------------------------
                        CASH RECEIPT
                        Supermarket
            
            --------------------------------------
            QTY	DESCRIPTION	       	PRICE   TOTAL
             1  Boots2              25,00   25,00
             3  West2               45,00  108,00
             4  West1               45,00  180,00
             2  Hat2                40,00   64,00
             3  Jacket2             35,00   84,00
            88  Shoes2              30,00  2112,00
            --------------------------------------
            Discount card No:123
            Discount card discount         385,95
            SALE discount                  592,00
            Total discount                 977,95
            TOTAL                         2187,05
            --------------------------------------
            """;

    @Test
    public void testGetDescriptionByIdShouldReturnId() throws IOException {
        //given
        Check check = new Check("testTask/1.txt");
        //when
        List<String> stringList = CheckServiceImpl.getInstance().createList(check);
        StringBuilder actual = new StringBuilder();
        for (int i=0; i<stringList.size(); i++) {
            if (i==4) {
                return;
            } else {
                actual.append(stringList.get(i)).append("\n");
            }
        }
        //then
        Assertions.assertEquals(EXPECTED, actual.toString());
    }

    Stream<String> generateCorrectProductList() {
        return Stream.of("30;яблоко;2.45;4", "26;Cherry;3.18;6", "39;Strawberry;100.00;8", "35;Nectarine;3.17;9");
    }

    @ParameterizedTest
    @MethodSource("generateCorrectProductList")
    void testShouldCheckRegexWithCorrectValues(String product) {
        boolean isValid = RegexValidator.isValid(product);
        Assertions.assertTrue(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"8;яЅлќко;10.12;02", "8;Єлка;10.12;2", "28;Apple;1.12;50", "28;APllE;1.12;2"})
    void testShouldCheckRegexWithIncorrectValues(String product) {
        boolean isValid = RegexValidator.isValid(product);
        Assertions.assertFalse(isValid);
    }

    @Test
    void testShouldParseParamsToGoodsAndCard() {
        String discountCardActual = CHECK_2.getDiscountCard();
        Assertions.assertEquals(DISCOUNT_CARD_EXPECTED, discountCardActual);
        Assertions.assertEquals(LIST_PM_EXPECTED1, CHECK_2.getCheckItemList());
    }

    @Test
    void testShouldReadPathAndReturnFIleContentAsString() {
        try {
            String[] actualContent = CheckServiceImpl.getInstance()
                    .getProductArrayFromFile("testTask/inputData.txt", "\n", "\n");
            Assertions.assertArrayEquals(EXPECTED_CONTENT, actualContent);
        } catch (IOException e) {
            System.out.println("! error !");
        }
    }

    @Test
    void testShouldCheckData() {
        Check check = CheckServiceImpl.getInstance()
                .checkProductsWithRegexAndWriteInvalidToFile(EXPECTED_CONTENT, "testTask/invalidData.txt");
        List<CheckItem> checkItemList = check.getCheckItemList();
        Assertions.assertEquals(LIST_PM_EXPECTED2, checkItemList);
    }
}
