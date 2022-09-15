package ru.clevertec.console.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.console.entities.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM check_products WHERE title=?1", nativeQuery = true)
    Product findByTitle(String title);

    @Modifying
    @Query(value = "UPDATE check_products SET title=?1, price=?2, discount=?3 WHERE id=?4", nativeQuery = true)
    int update(String title, double price, boolean discount, long id);
}
