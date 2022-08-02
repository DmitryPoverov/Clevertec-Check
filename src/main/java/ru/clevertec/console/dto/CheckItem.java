package ru.clevertec.console.dto;

import java.util.Objects;

public class CheckItem {

    private int id;
    private String name;
    private double price;
    private int quantity;

    public CheckItem() {
    }

    public CheckItem(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public CheckItem(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" + id + ", " + name + ", " + price + "$, " + quantity + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckItem checkItem = (CheckItem) o;
        return id == checkItem.id && Double.compare(checkItem.price, price) == 0 && quantity == checkItem.quantity && Objects.equals(name, checkItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, quantity);
    }
}
