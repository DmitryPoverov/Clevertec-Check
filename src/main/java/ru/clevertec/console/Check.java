package ru.clevertec.console;

import ru.clevertec.exception.WrongIdException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Check {

    private String discountCard;
    private List<ParamMapper> paramMappersList = new ArrayList<>();

    public Check() {
    }
    public Check(String[] args) {
        parseParamsToGoodsAndCard(args);
    }
    public Check(String path) throws IOException {
        String contentOfFile = convertPathStringToTextString(path, "");
        String[] argsFromFile = convertStringToArray(contentOfFile, ", ");
        parseParamsToGoodsAndCard(argsFromFile);
    }

    public String getDiscountCard() {
        return discountCard;
    }
    public List<ParamMapper> getParamMappersList() {
        return paramMappersList;
    }

    private void setParamMappersList(List<ParamMapper> paramMappersList) {
        this.paramMappersList = paramMappersList;
    }
    private void setDiscountCard(String discountCard) {
        this.discountCard = discountCard;
    }

    public void checkData(String[] strings, String invalidDataFilePath) {
        List<String> params = new ArrayList<>();
        try(FileWriter fileWriter = new FileWriter(invalidDataFilePath, false)) {
            StringBuilder wrongData = new StringBuilder();
            for (String s : strings) {
                if (isValid(s)) {
                    params.add(s);
                } else {
                    wrongData.append(s).append("\n");
                }
            }
            fileWriter.write(wrongData.toString());
            setParamMappersList(setParamMapper(params, ";"));
        } catch (IOException e) {
            System.out.println("!!! " + e.getMessage());
        }
    }

    public boolean isValid(String productString) {
        String regex = "^(100|[1-9]\\d?);([A-Z][a-z]{2,29}|[А-ЯЁ][а-яё]{2,29});(100\\.00|[1-9]\\d?\\.\\d{2});(20|1\\d|[1-9])$";
        return productString.matches(regex);
    }

    protected String[] convertStringToArray(String text, String regex) {
        return text.split(regex);
    }

    protected String convertPathStringToTextString(String path, String delimiter) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            sb.append(reader.lines().collect(Collectors.joining(delimiter)));
        }
        return sb.toString();
    }

// STREAM API
    public void printToFile(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            List<String> stringList = createList();

/* Old version.
                for (String s : stringList) {
                writer.write(s);
                writer.newLine();
            }*/

// Stream API.
            stringList.stream()
                    .peek(row -> {
                        try {
                            writer.write(row);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    })
                    .forEach(row -> {
                        try {
                            writer.newLine();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.out.println("!!! You entered the wrong path !!! ");
        }
    }

    private void parseParamsToGoodsAndCard(String[] args) {
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
        setDiscountCard(tempCard);
        setParamMappersList(setParamMapper(tempList, "-"));
    }

    protected List<ParamMapper> setParamMapper(List<String> params, String regex) {
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

    public List<String> printToStringList() {
        return createList();
    }

    public void printToConsoleFromFile() {
        System.out.println("--------------------------------------");
        System.out.println("            CASH RECEIPT");
        System.out.println("            Supermarket\n");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("                  " + formatter.format(new Date()));
        System.out.println("--------------------------------------");
        System.out.println("QTY DESCRIPTION         PRICE   TOTAL");
        double finalPrice = 0;
        for (ParamMapper pM : paramMappersList) {
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

    public List<String> createList() {
        int id;
        double price;
        int quantity;
        String description;
        int discountProductsCounter = 0;
        double fiveProductDiscount;
        double fiveProductsTotalDiscount = 0;
        double discountCardDiscount = discountCard.equals("") ? 0 : 0.15;
        double total;
        double totalDiscount;
        double totalPrice = 0;
        double finalPrice;
        List<String> stringsToPrint = new ArrayList<>();

        for (ParamMapper pM : paramMappersList) {
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

        for (ParamMapper pM : paramMappersList) {
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
        stringsToPrint.add("Discount card No:" + discountCard);
        stringsToPrint.add(String.format("Discount card discount %14.2f", totalDiscount));
        stringsToPrint.add(String.format("SALE discount %23.2f", fiveProductsTotalDiscount));
        stringsToPrint.add(String.format("Total discount %22.2f", fiveProductsTotalDiscount + totalDiscount));
        stringsToPrint.add(String.format("TOTAL %31.2f", finalPrice));
        stringsToPrint.add("--------------------------------------");
        return stringsToPrint;
    }
}
