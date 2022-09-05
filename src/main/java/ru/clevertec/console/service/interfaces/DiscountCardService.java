package ru.clevertec.console.service.interfaces;

import ru.clevertec.console.entities.DiscountCard;

import java.util.List;
import java.util.Optional;

public interface DiscountCardService{

    List<DiscountCard> findAll();

    Optional<DiscountCard> findById(long id) ;

    Optional<DiscountCard> findByNumber(String name) ;

    void deleteById(long id) ;

    void update(DiscountCard entity) ;

    DiscountCard save(DiscountCard entity) ;
}
