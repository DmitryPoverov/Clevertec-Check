package ru.clevertec.console;

import ru.clevertec.console.serviceClass.CheckService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Check {

    private transient CheckService service;
    private String discountCard;
    private List<CheckItem> checkItemList = new ArrayList<>();

    public Check(CheckService checkService) {
        service = checkService;
    }
    public Check(CheckService checkService, String[] arguments) {
        service = checkService;
        service.parseParamsToGoodsAndCard(arguments, this);
    }
    public Check(CheckService checkService, String path) throws IOException {
        service = checkService;
        String contentOfFile = service.convertPathStringToTextString(path, "");
        String[] argsFromFile = service.convertStringToArray(contentOfFile, ", ");
        service.parseParamsToGoodsAndCard(argsFromFile, this);
    }

    public Check(String discountCard, List<CheckItem> checkItemList) {
        this.discountCard = discountCard;
        this.checkItemList = checkItemList;
    }

    public void setCheckItemsList(List<CheckItem> checkItemList) {
        this.checkItemList = checkItemList;
    }
    public void setDiscountCard(String discountCard) {
        this.discountCard = discountCard;
    }
    public String getDiscountCard() {
        return discountCard;
    }
    public List<CheckItem> getCheckItemsList() {
        return checkItemList;
    }
    public CheckService getCheckService() {
        return service;
    }

    @Override
    public String toString() {
        return "Check{" + "Card:" + discountCard + ", Goods:" + checkItemList + '}';
    }
}
