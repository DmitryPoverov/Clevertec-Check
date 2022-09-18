package ru.clevertec.console.service.interfaces;

import ru.clevertec.console.dto.DiscountCardDto;

import java.util.List;
import java.util.Optional;

public interface DiscountCardService{

    List<DiscountCardDto> findAll();

    Optional<DiscountCardDto> findById(long id) ;

    Optional<DiscountCardDto> findByNumber(String number) ;

    void deleteById(long id) ;

    int update(DiscountCardDto entity) ;

    DiscountCardDto save(DiscountCardDto entity) ;
}
