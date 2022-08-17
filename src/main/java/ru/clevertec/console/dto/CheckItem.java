package ru.clevertec.console.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CheckItem {

    private int id;
    private String name;
    private double price;
    private int quantity;

    public CheckItem(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
