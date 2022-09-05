package ru.clevertec.console.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.repository.ProductsRepository;
import ru.clevertec.console.service.interfaces.ProductService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductsRepository repository;

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Product> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return Optional.ofNullable(repository.findByTitle(name));
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public void update(Product entity) {
        repository.update(entity.getTitle(), entity.getPrice(), entity.isDiscount(), entity.getId());
    }

    @Override
    public Product save(Product entity) {
        return repository.save(entity);
    }
}
