package ru.clevertec.console.serviceClass.entities;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Product {

    private int id;
    private String title;
    private double price;
    private boolean discount;
}
