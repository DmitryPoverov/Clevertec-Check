package ru.clevertec.console.serviceClass;

import ru.clevertec.console.Cards;
import ru.clevertec.console.Check;
import ru.clevertec.console.ParamMapper;
import ru.clevertec.console.Products;
import ru.clevertec.exception.WrongIdException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceClassInterfaceImpl implements CheckServiceInterface {

    public void parseParamsToGoodsAndCard(String[] args, Check check) {
        List<String> tempList = new ArrayList<>();
        String tempCard = "";
        for (String arg : args) {
            String temp1 = arg.replace(",", "");
            char[] c = temp1.toCharArray();
            if ((c[0] != 0) && (c[0] >= 48 && c[0] <= 57)) {
                tempList.add(temp1);
            } else if ((c[0] != 0) && ((c[0] == 'c') && Cards.isSuchCard(temp1))) {
                tempCard = arg.replace("card-", "");
            } else {
                System.out.println("!!! It seems like you entered a wrong card number or wrong format card!!!");
            }
        }
        check.setDiscountCard(tempCard);
        check.setParamMappersList(setParamMapper(tempList, "-"));
    }
    public void checkData(String[] strings, String invalidDataFilePath, Check check) {
        List<String> params = new ArrayList<>();
        try(FileWriter fileWriter = new FileWriter(invalidDataFilePath, false)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : strings) {
                if (isValid(s)) {
                    params.add(s);
                } else {
                    stringBuilder.append(s).append("\n");
                }
            }
            fileWriter.write(stringBuilder.toString());
            check.setParamMappersList(setParamMapper(params, ";"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public boolean isValid(String productString) {
        String regex = "^(100|[1-9]\\d?);([A-Z][a-z]{2,29}|[�-ߨ][�-��]{2,29});(100\\.00|[1-9]\\d?\\.\\d{2});(20|1\\d|[1-9])$";
        return productString.matches(regex);
    }
    public List<String> createList(Check check) {
        int id;
        double price;
        int quantity;
        String description;
        int discountProductsCounter = 0;
        double fiveProductDiscount;
        double fiveProductsTotalDiscount = 0;
        double discountCardDiscount = check.getDiscountCard().equals("") ? 0 : 0.15;
        double total;
        double totalDiscount;
        double totalPrice = 0;
        double finalPrice;
        List<String> stringsToPrint = new ArrayList<>();

        for (ParamMapper pM : check.getParamMappersList()) {
            id = pM.getId();
            quantity = pM.getQuantity();

            try {
                if (Products.isDiscount(id)) {
                    discountProductsCounter += quantity;
                }
            } catch (WrongIdException e) {
                System.out.println("!!! It seems like id=" + id + " is wrong !!!");
            }
        }

        stringsToPrint.add("--------------------------------------");
        stringsToPrint.add("            CASH RECEIPT");
        stringsToPrint.add("            Supermarket\n");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        stringsToPrint.add("                  " + formatter.format(new Date()));
        stringsToPrint.add("--------------------------------------");
        stringsToPrint.add("QTY DESCRIPTION         PRICE   TOTAL");

        for (ParamMapper pM : check.getParamMappersList()) {
            fiveProductDiscount = 0;
            id = pM.getId();

            try {
                description = Products.getDescriptionById(pM.getId());
                price = Products.getPriceById(pM.getId());
                quantity = pM.getQuantity();
                if (discountProductsCounter > 5) {
                    fiveProductDiscount = 0.2;
                }
                if (Products.isDiscount(id)) {
                    double fiveProductsCurrentDiscount = fiveProductDiscount * price * quantity;
                    fiveProductsTotalDiscount += fiveProductsCurrentDiscount;
                    total = price * quantity - fiveProductsCurrentDiscount;
                } else {
                    total = price * quantity;
                }
                totalPrice += total;

                stringsToPrint.add(String.format("%2d  %-17s %7.2f  %6.2f", quantity, description, price, total));
            } catch (WrongIdException ignored) {
            }
        }
        totalDiscount = totalPrice * discountCardDiscount;
        finalPrice = totalPrice - totalDiscount;

        stringsToPrint.add("--------------------------------------");
        stringsToPrint.add("Discount card No:" + check.getDiscountCard());
        stringsToPrint.add(String.format("Discount card discount %14.2f", totalDiscount));
        stringsToPrint.add(String.format("SALE discount %23.2f", fiveProductsTotalDiscount));
        stringsToPrint.add(String.format("Total discount %22.2f", fiveProductsTotalDiscount + totalDiscount));
        stringsToPrint.add(String.format("TOTAL %31.2f", finalPrice));
        stringsToPrint.add("--------------------------------------");
        return stringsToPrint;
    }
    public String convertPathStringToTextString(String path, String delimiter) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            sb.append(reader.lines().collect(Collectors.joining(delimiter)));
        }
        return sb.toString();
    }
    public void printToFile(String path, Check check) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            List<String> stringList = createList(check);
            for (String s : stringList) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("!!! You entered a wrong path!!! ");
        }
    }
    public List<ParamMapper> setParamMapper(List<String> params, String regex) {
        List<ParamMapper> paramMappers = new ArrayList<>();
        for (String line : params) {
            ParamMapper product = new ParamMapper();
            String[] products = line.split(regex);
            if (products.length==2) {
                for (int i = 0; i < products.length; i++) {
                    if (i == 0) {
                        product.setId(Integer.parseInt(products[i]));
                    } else {
                        product.setQuantity(Integer.parseInt(products[i]));
                    }
                }
            } else if (products.length==4) {
                for (int i = 0; i < products.length; i++) {
                    if (i == 0) {
                        product.setId(Integer.parseInt(products[i]));
                    } else if (i == 1) {
                        product.setName(products[i]);
                    } else if (i == 2) {
                        product.setPrice(Double.parseDouble(products[i]));
                    } else {
                        product.setQuantity(Integer.parseInt(products[i]));
                    }
                }
            }
            paramMappers.add(product);
        }
        return paramMappers;
    }
    public List<String> printToStringList(Check check) {
        return createList(check);
    }
    public String[] convertStringToArray(String text, String regex) {
        return text.split(regex);
    }
    public void printToConsoleFromFile(Check check) {
        System.out.println("--------------------------------------");
        System.out.println("            CASH RECEIPT");
        System.out.println("            Supermarket\n");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("                  " + formatter.format(new Date()));
        System.out.println("--------------------------------------");
        System.out.println("QTY DESCRIPTION         PRICE   TOTAL");
        double finalPrice = 0;
        for (ParamMapper pM : check.getParamMappersList()) {
            try {
                String description = pM.getName();
                int quantity = pM.getQuantity();
                double price = pM.getPrice();
                double total = quantity*price;
                finalPrice += total;

                System.out.printf("%2d  %-17s %7.2f  %6.2f%n", quantity, description, price, total);
            } catch (WrongIdException ignored) {
            }
        }
        System.out.println("--------------------------------------");
        System.out.printf("TOTAL %31.2f%n", finalPrice);
        System.out.println("--------------------------------------");
    }
}