package ru.clevertec.console.serviceClass;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import ru.clevertec.console.check.Check;
import ru.clevertec.console.dao.daoInterface.DiscountCardDao;
import ru.clevertec.console.dao.daoInterface.ProductDao;
import ru.clevertec.console.dao.implementations.DiscountCardDaoImpl;
import ru.clevertec.console.dao.implementations.ProductDaoImpl;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.validators.RegexValidator;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class CheckServiceImpl implements CheckService {

    private static CheckService instance;
    private static final DiscountCardDao<Integer, DiscountCard> DISCOUNT_CARD_DAO = DiscountCardDaoImpl.getInstance();
    private static final ProductDao<Integer, Product> DAO = ProductDaoImpl.getInstance();
    private static final String FILE_PATH = "/pdfCheck.pdf";

    private CheckServiceImpl() {
    }

    public static CheckService getInstance() {
        CheckService temporalInstance = instance;
        if (temporalInstance == null) {
            instance = temporalInstance = new CheckServiceImpl();
        }
        /* Creating a proxy object for my service class
        ClassLoader classLoader = temporalInstance.getClass().getClassLoader();
        Class<?>[] interfaces = temporalInstance.getClass().getInterfaces();
        return (CheckService) Proxy.newProxyInstance(classLoader, interfaces, new ServiceHandler(temporalInstance));*/
        return temporalInstance;
    }

    public int getNeededOffset(Integer pageSize, Integer pageNumber) {
        return pageSize * pageNumber - pageSize;
    }

    public String[] getArgArrayFromRequestParameters(Enumeration<String> parameterNames, Map<String, String[]> parameterMap) {
        List<String> methodArgs = new ArrayList<>();
        while (parameterNames.hasMoreElements()) {
            String s = parameterNames.nextElement();
            StringBuilder builder = new StringBuilder();
            if (s.contains("id")) {
                String[] id = parameterMap.get(s);
                if (!"".equals(id[0])) {
                    builder.append(id[0]).append("-");
                }
                if (parameterNames.hasMoreElements()) {
                    s = parameterNames.nextElement();
                    if (s.contains("quantity")) {
                        String[] quantity = parameterMap.get(s);
                        for (String s2 : quantity) {
                            if (!"".equals(s2)) {
                                builder.append(s2);
                            }
                        }
                    }
                }
            } else if (s.contains("discount")) {
                String[] discount = parameterMap.get(s);
                builder.append("card-").append(discount[0]);
            }
            if (!"".equals(builder.toString())) {
                methodArgs.add(builder.toString());
            }
        }
        return methodArgs.toArray(new String[0]);
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

    public Check getGoodsAndCard(String[] args) {
        List<String> tempList = new ArrayList<>();
        String tempCard = "";
        for (String arg : args) {
            String temp = arg.replace(", ", "");
            char[] c = temp.toCharArray();
            if ((c[0] != 0) && (c[0] >= 48 && c[0] <= 57)) {
                tempList.add(temp);
            } else {
                try {
                    if ((c[0] != 0) && (('c' == c[0]) && DISCOUNT_CARD_DAO.findByName(temp).isPresent())) {
                        tempCard = arg.replace("card-", "");
                    } else {
                        System.out.println("!!! It seems like you entered a wrong card number or wrong format card!!!");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Map<Product, Integer> productsAndQuantitiesMap = setProductsAndQuantityToMap(tempList, "-");
        return new Check(tempCard, productsAndQuantitiesMap);
    }

    public Check checkProductsWithRegexAndWriteInvalidToFile(String[] strings, String invalidDataFilePath) {
        List<String> tempList = new ArrayList<>();
        Check check = new Check();
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
            Map<Product, Integer> productsAndQuantitiesMap = setProductsAndQuantityToMap(tempList, ";");
            check.setCheckItemMap(productsAndQuantitiesMap);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return check;
    }

    public List<String> createList(Check check) {
        int id;
        double price;
        int quantity;
        String description;
        int discountProductsCounter = 0;
        double fiveProductDiscount;
        double fiveProductsTotalDiscount = 0;
        double discountCardDiscount = "".equals(check.getDiscountCard()) ? 0 : 0.15;
        double total;
        double totalDiscount;
        double totalPrice = 0;
        double finalPrice;
        List<String> stringsToPrint = new ArrayList<>();

        for (Product product : check.getCheckItemMap().keySet()) {
            id = product.getId();
            quantity = check.getCheckItemMap().get(product);

            try {
                if (DAO.findById(id).isPresent() && DAO.findById(id).get().isDiscount()) {
                    discountProductsCounter += quantity;
                }
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

        for (Product checkItem : check.getCheckItemMap().keySet()) {
            fiveProductDiscount = 0;
            id = checkItem.getId();
            try {
                if (DAO.findById(checkItem.getId()).isPresent()) {
                    description = DAO.findById(checkItem.getId()).get().getTitle();
                    price = DAO.findById(checkItem.getId()).get().getPrice();
                    quantity = check.getCheckItemMap().get(checkItem);
                    if (discountProductsCounter > 5) {
                        fiveProductDiscount = 0.2;
                    }
                    if (DAO.findById(id).isPresent() && DAO.findById(id).get().isDiscount()) {
                        double fiveProductsCurrentDiscount = fiveProductDiscount * price * quantity;
                        fiveProductsTotalDiscount += fiveProductsCurrentDiscount;
                        total = price * quantity - fiveProductsCurrentDiscount;
                    } else {
                        total = price * quantity;
                    }
                    totalPrice += total;

                    stringsToPrint.add(String.format("%2d  %-17s %7.2f  %6.2f", quantity, description, price, total));
                }


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

    public String[] getProductArrayFromFile(String path, String delimiter, String regex) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            sb.append(reader.lines().collect(Collectors.joining(delimiter)));
        }
        return sb.toString().split(regex);
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

    @Override
    public Map<Product, Integer> setProductsAndQuantityToMap(List<String> params, String regex) {
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

    public void printToConsoleFromFile(Check check) {
        System.out.println("--------------------------------------");
        System.out.println("            CASH RECEIPT");
        System.out.println("            Supermarket\n");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("                  " + formatter.format(new Date()));
        System.out.println("--------------------------------------");
        System.out.println("QTY DESCRIPTION         PRICE   TOTAL");
        double finalPrice = 0;
        for (Product product : check.getCheckItemMap().keySet()) {

            String description = product.getTitle();
            int quantity = check.getCheckItemMap().get(product);
            double price = product.getPrice();
            double total = quantity * price;
            finalPrice += total;

            System.out.printf("%2d  %-17s %7.2f  %6.2f%n", quantity, description, price, total);
        }
        System.out.println("--------------------------------------");
        System.out.printf("TOTAL %31.2f%n", finalPrice);
        System.out.println("--------------------------------------");
    }
}
