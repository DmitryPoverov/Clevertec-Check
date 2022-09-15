package ru.clevertec.console.service.interfaces;

import ru.clevertec.console.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(long id);

    Optional<Product> findByName(String name);

    void deleteById(long id);

    Product update(Product product);

    Product save(Product product);
}
