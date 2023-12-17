package ru.clevertec.console.service.interfaces;

import ru.clevertec.console.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductDto> findAll();

    Optional<ProductDto> findById(long id);

    Optional<ProductDto> findByName(String name);

    void deleteById(long id);

    ProductDto update(ProductDto product);

    ProductDto save(ProductDto product);
}
