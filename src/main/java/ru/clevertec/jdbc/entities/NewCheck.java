package ru.clevertec.jdbc.entities;

import ru.clevertec.console.Check;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewCheck extends Check {

    private static final NewCheck INSTANCE = new NewCheck();

    private List<Product> productList = new ArrayList<>();
    private DiscountCard discountCard = new DiscountCard();

    private NewCheck() {
        super();
    }

    public NewCheck(String[] args) {
        super(args);
    }

    public NewCheck(String path) throws IOException {
        super(path);
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
