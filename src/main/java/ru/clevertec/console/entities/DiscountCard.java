package ru.clevertec.console.entities;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DiscountCard {

    private int id;
    private String number;
}
