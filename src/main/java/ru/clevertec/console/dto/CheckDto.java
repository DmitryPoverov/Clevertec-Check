package ru.clevertec.console.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.console.entities.Product;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckDto {

    private String discountCard;
    private Map<Product, Integer> checkItemMap;
}
