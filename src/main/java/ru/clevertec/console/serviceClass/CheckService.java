package ru.clevertec.console.serviceClass;

import ru.clevertec.console.check.Check;
import ru.clevertec.console.serviceClass.entities.Product;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public interface CheckService {

    int getNeededOffset(Integer pageSize, Integer pageNumber);

    String[] getArgArrayFromRequestParameters(Enumeration<String> parameterNames, Map<String, String[]> parameterMap);

    void printToPDF(List<String> list);

    Check getGoodsAndCard(String[] arguments);

    Check checkProductsWithRegexAndWriteInvalidToFile(String[] strings, String invalidDataFilePath);

    List<String> createList(Check check);

    void printToFile(Check check, String path);

    String[] getProductArrayFromFile(String path, String delimiter, String regex) throws IOException;

    Map<Product, Integer> setProductsAndQuantityToMap(List<String> params, String regex);

    void printToConsoleFromFile(Check check);
}
