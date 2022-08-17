package ru.clevertec.console;

import ru.clevertec.console.check.Check;
import ru.clevertec.console.serviceClass.CheckServiceImpl;

import java.io.IOException;

public class CheckRunner {

    private static final String IDENT = "\n";

    public static void main(String[] args) throws IOException {

        if (args[0].equals("--f")) {                                        // for 1.txt {13-1, 8-3, 9-4, ...}
            String path = args[1];
            Check check = new Check(path);
            CheckServiceImpl.getInstance().printToFile(check, args[2]);
        } else if (args[0].equals("--s")) {                                 // for inputData.txt {28;Apple;1.12;2\n...}
            String path = args[1];
            String[] productArray = CheckServiceImpl.getInstance().getProductArrayFromFile(path, IDENT, IDENT);
            Check check = CheckServiceImpl.getInstance().checkProductsWithRegexAndWriteInvalidToFile(productArray, args[2]);
            CheckServiceImpl.getInstance().printToConsoleFromFile(check);
        } else {                                                            // for console {1-2 2-2 3-3...}
            Check check = new Check(args);
            for (String s : CheckServiceImpl.getInstance().createList(check)) {
                System.out.println(s);
            }
        }
    }
}
