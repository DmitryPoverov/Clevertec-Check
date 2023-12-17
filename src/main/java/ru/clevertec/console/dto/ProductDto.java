package ru.clevertec.console.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "title", "price"})
@Setter
@Getter
@ToString
public class ProductDto {

    private long id;
    private String title;
    private double price;
    private boolean discount;
}

