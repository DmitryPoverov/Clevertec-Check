package ru.clevertec.console.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clevertec.console.dao.daoInterface.DiscountCardDao;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.service.interfaces.DiscountCardService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class DiscountCardServiceImpl implements DiscountCardService<Integer, DiscountCard> {

    @Autowired
    private DiscountCardDao<Integer, DiscountCard> dao;

    public DiscountCardServiceImpl(DiscountCardDao<Integer, DiscountCard> dao) {
        this.dao = dao;
    }

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
