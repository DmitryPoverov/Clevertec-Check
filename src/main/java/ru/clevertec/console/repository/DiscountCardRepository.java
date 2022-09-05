package ru.clevertec.console.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.console.entities.DiscountCard;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Long> {

    @Query(value = "SELECT * FROM check_discount_card WHERE number=?1", nativeQuery = true)
    DiscountCard findByNumber(String number);

    @Query(value = "UPDATE check_discount_card SET number=?1 WHERE id=?2", nativeQuery = true)
    void update(String number, long id);



}
