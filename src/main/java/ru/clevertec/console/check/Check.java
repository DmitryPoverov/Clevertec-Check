package ru.clevertec.console.check;

import lombok.Getter;
import lombok.Setter;
import ru.clevertec.console.dto.CheckItem;
import ru.clevertec.console.serviceClass.CheckServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Check {

    private static final String DELIMITER = "";
    private static final String REGEX = ", ";

    private String discountCard;
    private List<CheckItem> checkItemList = new ArrayList<>();

    public Check() {}

    public Check(String[] arguments) {
        Check goodsAndCard = CheckServiceImpl.getInstance().getGoodsAndCard(arguments);
        discountCard = goodsAndCard.getDiscountCard();
        checkItemList = goodsAndCard.getCheckItemList();
    }
    public Check(String path) throws IOException {
        String[] productArray = CheckServiceImpl.getInstance().getProductArrayFromFile(path, DELIMITER, REGEX);
        CheckServiceImpl.getInstance().getGoodsAndCard(productArray);
    }

    public Check(String discountCard, List<CheckItem> checkItemList) {
        this.discountCard = discountCard;
        this.checkItemList = checkItemList;
    }

    @Override
    public String toString() {
        return "Check{" + "Card:" + discountCard + ", Goods:" + checkItemList + '}';
    }
}
