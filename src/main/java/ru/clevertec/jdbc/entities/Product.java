package ru.clevertec.jdbc.entities;

import java.util.Objects;

public class Product {

    private int id;
    private String title;
    private double price;
    private boolean discount;

    public Product() {
    }

    public Product(String title, double price, boolean discount) {
        this.title = title;
        this.price = price;
        this.discount = discount;
    }

    public Product(int id, String title, double price, boolean discount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public double getPrice() {
        return price;
    }
    public boolean isDiscount() {
        return discount;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Products{" +
               "id=" + id +
               ", name='" + title + '\'' +
               ", price=" + price +
               ", discount=" + discount +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Double.compare(product.price, price) == 0 && discount == product.discount && Objects.equals(title, product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, discount);
    }
}
