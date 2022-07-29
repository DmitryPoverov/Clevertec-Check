package ru.clevertec.jdbc.entities;

import java.util.Objects;

public class DiscountCard {

    private int id;
    private int number;

    public DiscountCard() {
    }

    public DiscountCard(int number) {
        this.number = number;
    }

    public DiscountCard(int id, int number) {
        this.id = id;
        this.number = number;
    }

    public int getId() {
        return id;
    }
    public int getNumber() {
        return number;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Discount Card: " + number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountCard that = (DiscountCard) o;
        return id == that.id && number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }
}
