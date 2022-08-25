package ru.clevertec.console;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.console.config.AppConfig;
import ru.clevertec.console.entities.Check;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.service.interfaces.CheckService;
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

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        CheckService<String, Check> checkServiceBean = context.getBean("checkServiceImpl", CheckService.class);

        if (args[0].equals("--f")) {                                        // for 1.txt {13-1, 8-3, 9-4, ...}
            String path = args[1];
            String[] productArray = CheckUtil.getProductArrayFromFile(path, DELIMITER, REGEX);

            List<String> list = checkServiceBean.handleArrayAndGetStrungList(productArray);
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
            List<String> strings = checkServiceBean.handleArrayAndGetStrungList(args);
            for (String s : strings) {
                System.out.println(s);
            }
        }
    }
}
