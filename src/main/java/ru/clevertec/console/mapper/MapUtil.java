package ru.clevertec.console.mapper;

import org.springframework.stereotype.Service;
import ru.clevertec.console.dto.DiscountCardDto;
import ru.clevertec.console.dto.ProductDto;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.entities.Product;

@Service
public class MapUtil {

    public DiscountCard mapDtoToDiscountCard(DiscountCardDto dto) {
        return DiscountCard.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .build();
    }

    public DiscountCardDto mapDiscountCardToDto(DiscountCard card) {
        return DiscountCardDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .build();
    }

    public Product mapDtoToProduct(ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .discount(dto.isDiscount())
                .build();
    }

    public ProductDto mapProductToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .discount(product.isDiscount())
                .build();
    }
}
