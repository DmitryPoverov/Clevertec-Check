package ru.clevertec.console.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.console.dao.daoInterface.ProductDao;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.service.interfaces.ProductService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService<Integer, Product> {

    private final ProductDao<Integer, Product> dao;

    @Override
    public List<Product> findAll(Integer pageSize, Integer pageNumber) throws SQLException {
        return dao.findAll(pageSize, pageNumber);
    }

    @Override
    public Optional<Product> findById(Integer id) throws SQLException {
        return dao.findById(id);
    }

    @Override
    public Optional<Product> findByName(String name) throws SQLException {
        return dao.findByName(name);
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        return dao.deleteById(id);
    }

    @Override
    public boolean update(Product entity) throws SQLException {
        return dao.update(entity);
    }

    @Override
    public Product save(Product entity) throws SQLException {
        return dao.save(entity);
    }
}
