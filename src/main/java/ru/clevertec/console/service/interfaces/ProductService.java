package ru.clevertec.console.service.interfaces;

import ru.clevertec.console.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(long id);

    Optional<Product> findByName(String name);

    void deleteById(long id);

    void update(Product entity);

    Product save(Product entity);
}
