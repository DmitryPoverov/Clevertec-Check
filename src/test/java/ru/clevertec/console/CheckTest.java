package ru.clevertec.console;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckTest {

    private static final Check check1 = new Check();
    private static final String[] args = new String[]{"1-2", "2-2", "card-123"};
    private static final Check check2 = new Check(args);
    private static final String DISCOUNT_CARD_EXPECTED = "123";
    private static final ParamMapper PM1 = new ParamMapper(1, 2);
    private static final ParamMapper PM2 = new ParamMapper(2, 2);
    private static final List<ParamMapper> LIST_PM_EXPECTED = Arrays.asList(PM1, PM2);
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
        List<String> stringList = check.printToStringList();
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
        return Stream.of("30;Яблоко;2.45;4", "26;Cherry;3.18;6", "39;Strawberry;100.00;8", "35;Nectarine;3.17;9");
    }

    @ParameterizedTest
    @MethodSource("generateCorrectProductList")
    void testShouldCheckRegexWithCorrectValues(String product) {
        boolean isValid = check1.isValid(product);
        Assertions.assertTrue(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"8;ЯБлОко;10.12;02", "8;ёлка;10.12;2", "28;Apple;1.12;50", "28;APllE;1.12;2"})
    void testShouldCheckRegexWithIncorrectValues(String product) {
        boolean isValid = check1.isValid(product);
        Assertions.assertFalse(isValid);
    }

    @Test
    void testShouldParseParamsToGoodsAndCard() {
        String discountCardActual = check2.getDiscountCard();
// Pavel, is it normal putting two Assertions in one test?
        Assertions.assertEquals(DISCOUNT_CARD_EXPECTED, discountCardActual);
        Assertions.assertEquals(LIST_PM_EXPECTED, check2.getParamMappersList());

    }
}
