package ru.clevertec.console.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.repository.ProductsRepository;
import ru.clevertec.console.service.interfaces.ProductService;

import java.util.List;
import java.util.Optional;

@Service
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
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public int update(Product product) {
        return repository.update(product.getTitle(), product.getPrice(), product.isDiscount(), product.getId());
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return repository.save(product);
    }
}
