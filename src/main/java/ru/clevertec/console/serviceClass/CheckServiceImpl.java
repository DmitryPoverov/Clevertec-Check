package ru.clevertec.console.serviceClass;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import ru.clevertec.console.check.Check;
import ru.clevertec.console.dao.daoInterface.Dao;
import ru.clevertec.console.dao.implementations.DiscountCardDao;
import ru.clevertec.console.dao.implementations.ProductsDao;
import ru.clevertec.console.dto.CheckItem;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.exception.WrongIdException;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class CheckServiceImpl implements CheckService {

    private static CheckService instance;
    private static final Dao<Integer, Product> DAO = ProductsDao.getInstance();
    private static final String FILE_PATH = "C:/Users/PD/IdeaProjects/Clevertec Check/src/main/webapp/resources/pdf_check.pdf";

    private CheckServiceImpl() {
    }

    public static CheckService getInstance() {
        CheckService temporalInstance = instance;
        if (temporalInstance == null) {
            instance = temporalInstance = new CheckServiceImpl();
        }
        return temporalInstance;
    }

    public String[] getArgsList(Enumeration<String> parameterNames, Map<String, String[]> parameterMap) {
        List<String> postMethodArgs = new ArrayList<>();
        while (parameterNames.hasMoreElements()) {
            StringBuilder builder = new StringBuilder();
            String s = parameterNames.nextElement();
            if (s.contains("id")) {
                String[] id = parameterMap.get(s);
                builder.append(id[0]).append("-");
                if (parameterNames.hasMoreElements()) {
                    s = parameterNames.nextElement();
                    if (s.contains("quantity")) {
                        String[] quantity = parameterMap.get(s);
                        for (String s2 : quantity) {
                            builder.append(s2);
                        }
                    }
                }
            } else if (s.contains("discount")) {
                String[] discount = parameterMap.get(s);
                builder.append("card-").append(discount[0]);
            }
            postMethodArgs.add(builder.toString());
        }
        return postMethodArgs.toArray(new String[0]);
    }

    public void printToPDF(List<String> list) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE_PATH));
            Font font = new Font();
            Rectangle a5 = PageSize.A5;
            font.setFamily("Courier");
            document.setPageSize(a5);
            document.open();
            for (String s : list) {
                document.add(new Paragraph(s, font));
            }
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
    }

    public void parseParamsToGoodsAndCard(String[] args, Check check) {
        Dao<Integer, DiscountCard> dao = DiscountCardDao.getInstance();
        List<String> tempList = new ArrayList<>();
        String tempCard = "";
        for (String arg : args) {
            String temp = arg.replace(",", "");
            char[] c = temp.toCharArray();
            if ((c[0] != 0) && (c[0] >= 48 && c[0] <= 57)) {
                tempList.add(temp);
            } else {
                try {
                    if ((c[0] != 0) && ((c[0] == 'c') && dao.isSuchCard(temp))) {
                        tempCard = arg.replace("card-", "");
                    } else {
                        System.out.println("!!! It seems like you entered a wrong card number or wrong format card!!!");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        check.setDiscountCard(tempCard);
        check.setCheckItemsList(setParamMapper(tempList, "-"));
    }

    public void checkData(String[] strings, String invalidDataFilePath, Check check) {
        List<String> params = new ArrayList<>();
        try (FileWriter fileWriter = new FileWriter(invalidDataFilePath, false)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : strings) {
                if (isValid(s)) {
                    params.add(s);
                } else {
                    stringBuilder.append(s).append("\n");
                }
            }
            fileWriter.write(stringBuilder.toString());
            check.setCheckItemsList(setParamMapper(params, ";"));
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

        for (CheckItem pM : check.getCheckItemsList()) {
            id = pM.getId();
            quantity = pM.getQuantity();

            try {
                if (DAO.isDiscountById(id)) {
                    discountProductsCounter += quantity;
                }
            } catch (WrongIdException e) {
                System.out.println("!!! It seems like id=" + id + " is wrong !!!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        stringsToPrint.add("--------------------------------------");
        stringsToPrint.add("            CASH RECEIPT");
        stringsToPrint.add("            Supermarket\n");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        stringsToPrint.add("                  " + formatter.format(new Date()));
        stringsToPrint.add("--------------------------------------");
        stringsToPrint.add("QTY DESCRIPTION         PRICE   TOTAL");

        for (CheckItem pM : check.getCheckItemsList()) {
            fiveProductDiscount = 0;
            id = pM.getId();

            try {
                description = DAO.getNameById(pM.getId());
                price = DAO.getPriceById(pM.getId());
                quantity = pM.getQuantity();
                if (discountProductsCounter > 5) {
                    fiveProductDiscount = 0.2;
                }
                if (DAO.isDiscountById(id)) {
                    double fiveProductsCurrentDiscount = fiveProductDiscount * price * quantity;
                    fiveProductsTotalDiscount += fiveProductsCurrentDiscount;
                    total = price * quantity - fiveProductsCurrentDiscount;
                } else {
                    total = price * quantity;
                }
                totalPrice += total;

                stringsToPrint.add(String.format("%2d  %-17s %7.2f  %6.2f", quantity, description, price, total));
            } catch (WrongIdException ignored) {
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
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

    public void printToFile(Check check, String path) {
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

    public List<CheckItem> setParamMapper(List<String> params, String regex) {
        List<CheckItem> checkItems = new ArrayList<>();
        for (String line : params) {
            CheckItem checkItem = new CheckItem();
            String[] paramsOfItem = line.split(regex);
            if (paramsOfItem.length == 2) {
                for (int i = 0; i < paramsOfItem.length; i++) {
                    if (i == 0) {
                        checkItem.setId(Integer.parseInt(paramsOfItem[i]));
                    } else {
                        checkItem.setQuantity(Integer.parseInt(paramsOfItem[i]));
                    }
                }
            } else if (paramsOfItem.length == 4) {
                for (int i = 0; i < paramsOfItem.length; i++) {
                    if (i == 0) {
                        checkItem.setId(Integer.parseInt(paramsOfItem[i]));
                    } else if (i == 1) {
                        checkItem.setName(paramsOfItem[i]);
                    } else if (i == 2) {
                        checkItem.setPrice(Double.parseDouble(paramsOfItem[i]));
                    } else {
                        checkItem.setQuantity(Integer.parseInt(paramsOfItem[i]));
                    }
                }
            }
            checkItems.add(checkItem);
        }
        return checkItems;
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
        for (CheckItem pM : check.getCheckItemsList()) {
            try {
                String description = pM.getName();
                int quantity = pM.getQuantity();
                double price = pM.getPrice();
                double total = quantity * price;
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
