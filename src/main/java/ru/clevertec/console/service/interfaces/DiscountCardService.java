package ru.clevertec.console.service.interfaces;

import ru.clevertec.console.entities.DiscountCard;

import java.util.List;
import java.util.Optional;

public interface DiscountCardService{

    List<DiscountCard> findAll();

    Optional<DiscountCard> findById(long id) ;

    Optional<DiscountCard> findByNumber(String number) ;

    void deleteById(long id) ;

    int update(DiscountCard entity) ;

    DiscountCard save(DiscountCard entity) ;
}
