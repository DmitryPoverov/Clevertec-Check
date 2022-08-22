package ru.clevertec.console.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Check {

    private String discountCard;
    private Map<Product, Integer> checkItemMap;
}
