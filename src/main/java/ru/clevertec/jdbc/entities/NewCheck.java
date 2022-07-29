package ru.clevertec.jdbc.entities;

import java.util.ArrayList;
import java.util.List;

public class NewCheck /*extends Check */{

    private static final NewCheck INSTANCE = new NewCheck();
    private List<Product> productList = new ArrayList<>();
    private DiscountCard discountCard = new DiscountCard();

    private NewCheck() {
    }

    public static NewCheck getInstance() {
        return INSTANCE;
    }

    public List<Product> getProductList() {
        return productList;
    }
    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }
}
