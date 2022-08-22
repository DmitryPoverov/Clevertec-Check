package ru.clevertec.console;

import ru.clevertec.console.dao.implementations.DiscountCardDaoImpl;
import ru.clevertec.console.dao.implementations.ProductDaoImpl;
import ru.clevertec.console.entities.Check;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.service.implementations.CheckServiceImpl;
import ru.clevertec.console.service.implementations.DiscountCardServiceImpl;
import ru.clevertec.console.service.implementations.ProductServiceImpl;
import ru.clevertec.console.utils.CheckUtil;
import ru.clevertec.console.utils.PrintUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CheckRunner {

    private static final String IDENT = "\n";
    private static final String DELIMITER = "";
    private static final String REGEX = ", ";

    public static void main(String[] args) throws IOException {

        if (args[0].equals("--f")) {                                        // for 1.txt {13-1, 8-3, 9-4, ...}
            String path = args[1];
            String[] productArray = CheckUtil.getProductArrayFromFile(path, DELIMITER, REGEX);

            List<String> list = new CheckServiceImpl(
                    new DiscountCardServiceImpl(new DiscountCardDaoImpl()),
                    new ProductServiceImpl(new ProductDaoImpl()))
                    .handleArrayAndGetStrungList(productArray);

            PrintUtil.printToFile(list, args[2]);

        } else if (args[0].equals("--s")) {                                 // for inputData.txt {28;Apple;1.12;2\n...}
            Check check = new Check();
            String path = args[1];
            String outputPath = args[2];

            String[] array = CheckUtil.getProductArrayFromFile(path, IDENT, IDENT);
            Map<Product, Integer> productMap = CheckUtil.checkProductsGetMapWriteInvalidToFile(array, outputPath);

            check.setCheckItemMap(productMap);

            PrintUtil.printToConsoleFromFile(check);

        } else {                                                            // for console {1-2 2-2 3-3...}
            List<String> strings = new CheckServiceImpl(
                    new DiscountCardServiceImpl(new DiscountCardDaoImpl()),
                    new ProductServiceImpl(new ProductDaoImpl()))
                    .handleArrayAndGetStrungList(args);

            for (String s : strings) {
                System.out.println(s);
            }
        }
    }
}
