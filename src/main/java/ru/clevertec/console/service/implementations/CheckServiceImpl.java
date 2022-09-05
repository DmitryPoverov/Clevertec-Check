package ru.clevertec.console.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.console.entities.Check;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.service.interfaces.CheckService;
import ru.clevertec.console.service.interfaces.DiscountCardService;
import ru.clevertec.console.service.interfaces.ProductService;
import ru.clevertec.console.utils.CheckUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService<String, Check> {

    private final DiscountCardService discountCardService;
    private final ProductService productService;

    /* Creating a proxy object for my service class
    public CheckService<String, Check> getInstance() {
        CheckService<String, Check> tempCheckService = new CheckServiceImpl(
                new DiscountCardServiceImpl(new DiscountCardDaoImpl()),
                new ProductServiceImpl(new ProductDaoImpl()));
        ClassLoader classLoader = tempCheckService.getClass().getClassLoader();
        Class<?>[] interfaces = tempCheckService.getClass().getInterfaces();
        return (CheckService<String, Check>) Proxy.newProxyInstance(classLoader, interfaces, new ServiceHandler(tempCheckService));
    }*/

    @Override
    public List<String> handleArrayAndGetStrungList(String[] args) {
        Check goodsAndCard = getGoodsAndCard(args);
        return createList(goodsAndCard);
    }

    @Override
    public Check getGoodsAndCard(String[] args) {
        List<String> tempList = new ArrayList<>();
        String tempCard = "";
        for (String arg : args) {
            String temp = arg.replace(", ", "");
            char[] c = temp.toCharArray();
            if ((c[0] != 0) && (c[0] >= 48 && c[0] <= 57)) {
                tempList.add(temp);
            } else {

                if ((c[0] != 0) && (('c' == c[0]) && discountCardService.findByNumber(temp).isPresent())) {
                    tempCard = arg.replace("card-", "");
                } else {
                    System.out.println("!!! It seems like you entered a wrong card number or wrong format card!!!");
                }
            }
        }
        Map<Product, Integer> productsAndQuantitiesMap = CheckUtil.getProductAndQuantityMap(tempList, "-");
        return new Check(tempCard, productsAndQuantitiesMap);
    }

    @Override
    public List<String> createList(Check check) {
        long id;
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


            if (productService.findById(id).isPresent() && productService.findById(id).get().isDiscount()) {
                discountProductsCounter += quantity;

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

            if (productService.findById(checkItem.getId()).isPresent()) {
                description = productService.findById(checkItem.getId()).get().getTitle();
                price = productService.findById(checkItem.getId()).get().getPrice();
                quantity = check.getCheckItemMap().get(checkItem);
                if (discountProductsCounter > 5) {
                    fiveProductDiscount = 0.2;
                }
                if (productService.findById(id).isPresent() && productService.findById(id).get().isDiscount()) {
                    double fiveProductsCurrentDiscount = fiveProductDiscount * price * quantity;
                    fiveProductsTotalDiscount += fiveProductsCurrentDiscount;
                    total = price * quantity - fiveProductsCurrentDiscount;
                } else {
                    total = price * quantity;
                }
                totalPrice += total;

                stringsToPrint.add(String.format("%2d  %-17s %7.2f  %6.2f", quantity, description, price, total));
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
}
