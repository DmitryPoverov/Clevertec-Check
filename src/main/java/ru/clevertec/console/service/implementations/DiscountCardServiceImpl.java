package ru.clevertec.console.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.console.dao.daoInterface.DiscountCardDao;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.service.interfaces.DiscountCardService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService<Integer, DiscountCard> {

    private final DiscountCardDao<Integer, DiscountCard> dao;

    @Override
    public List <DiscountCard> findAll(Integer pageSize, Integer pageNumber) throws SQLException {
        return dao.findAll(pageSize, pageNumber);
    }

    @Override
    public Optional<DiscountCard> findById(Integer id) throws SQLException {
        return dao.findById(id);
    }

    @Override
    public Optional<DiscountCard> findByName(String name) throws SQLException {
        return dao.findByName(name);
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        return dao.deleteById(id);
    }

    @Override
    public boolean update(DiscountCard entity) throws SQLException {
        return dao.update(entity);
    }

    @Override
    public DiscountCard save(DiscountCard entity) throws SQLException {
        return dao.save(entity);
    }
}
