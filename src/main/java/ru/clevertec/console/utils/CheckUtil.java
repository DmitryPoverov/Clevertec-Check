package ru.clevertec.console.utils;

import ru.clevertec.console.entities.Product;
import ru.clevertec.console.validators.RegexValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckUtil {

    public CheckUtil() {}

/* TODO: this method for runner instead of two
    public static Map<Product, Integer> getMapFromPath(String path, String delimiter, String regex, String invalidDataFilePath) throws IOException {
        String[] productArray = getProductArrayFromFile(path, delimiter, regex);
        return checkProductsWithRegexAndWriteInvalidToFile(productArray, invalidDataFilePath);
    }*/

    public static String[] getProductArrayFromFile(String path, String delimiter, String regex) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            sb.append(reader.lines().collect(Collectors.joining(delimiter)));
        }
        return sb.toString().split(regex);
    }

    public static Map<Product, Integer> checkProductsGetMapWriteInvalidToFile(String[] strings, String invalidDataFilePath) {
        List<String> tempList = new ArrayList<>();
        Map<Product, Integer> productMap = new LinkedHashMap<>();
        try (FileWriter fileWriter = new FileWriter(invalidDataFilePath, false)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : strings) {
                if (RegexValidator.isValid(s)) {
                    tempList.add(s);
                } else {
                    stringBuilder.append(s).append("\n");
                }
            }
            fileWriter.write(stringBuilder.toString());
            productMap = CheckUtil.getProductAndQuantityMap(tempList, ";");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return productMap;
    }

    public static Map<Product, Integer> getProductAndQuantityMap(List<String> params, String regex) {
        Map<Product, Integer> productMap = new LinkedHashMap<>();
        Product product = Product.builder().build();
        int quantity = 0;
        for (String line : params) {
            String[] paramsOfItem = line.split(regex);
            if (paramsOfItem.length == 2) {
                for (int i = 0; i < paramsOfItem.length; i++) {
                    if (i == 0) {
                        product.setId(Integer.parseInt(paramsOfItem[i]));
                    } else {
                        quantity = Integer.parseInt(paramsOfItem[i]);
                    }
                }
            } else if (paramsOfItem.length == 4) {
                for (int i = 0; i < paramsOfItem.length; i++) {
                    if (i == 0) {
                        product.setId(Integer.parseInt(paramsOfItem[i]));
                    } else if (i == 1) {
                        product.setTitle(paramsOfItem[i]);
                    } else if (i == 2) {
                        product.setPrice(Double.parseDouble(paramsOfItem[i]));
                    } else {
                        quantity = Integer.parseInt(paramsOfItem[i]);
                    }
                }
            }
            productMap.put(product, quantity);
            product = Product.builder().build();
            quantity = 0;
        }
        return productMap;
    }
}
