package ru.clevertec.console.entities;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CheckItem {

    private int id;
    private String title;
    private double price;
    private int quantity;

    public CheckItem(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
