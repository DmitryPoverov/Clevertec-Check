package ru.clevertec.console.serviceClass.entities;

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
