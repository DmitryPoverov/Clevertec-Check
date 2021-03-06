package by.poverovClevertec.console;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CheckTest {

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
    public void testGetDescriptionByIdShouldReturnId() {
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
        Assert.assertEquals(EXPECTED, actual.toString());
    }
}
