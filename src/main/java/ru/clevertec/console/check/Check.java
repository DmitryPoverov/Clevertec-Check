package ru.clevertec.console.check;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.console.serviceClass.entities.Product;
import ru.clevertec.console.serviceClass.CheckServiceImpl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Check {

    private static final String DELIMITER = "";
    private static final String REGEX = ", ";

    private String discountCard;
    private Map<Product, Integer> checkItemMap = new LinkedHashMap<>();

    public Check(String[] arguments) {
        Check goodsAndCard = CheckServiceImpl.getInstance().getGoodsAndCard(arguments);
        discountCard = goodsAndCard.getDiscountCard();
        checkItemMap = goodsAndCard.getCheckItemMap();
    }

    public Check(String path) throws IOException {
        String[] productArray = CheckServiceImpl.getInstance().getProductArrayFromFile(path, DELIMITER, REGEX);
        Check goodsAndCard = CheckServiceImpl.getInstance().getGoodsAndCard(productArray);
        discountCard = goodsAndCard.getDiscountCard();
        checkItemMap = goodsAndCard.getCheckItemMap();
    }

    @Override
    public String toString() {
        return "Check{" +
               "discountCard='" + discountCard + '\'' +
               ", checkItemMap=" + checkItemMap +
               '}';
    }
}
