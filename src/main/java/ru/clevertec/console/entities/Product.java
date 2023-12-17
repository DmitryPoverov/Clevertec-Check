package ru.clevertec.console.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = {"id", "title"})
@Entity
@Table(name = "check_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name ="title")
    private String title;

    @Column(name = "price")
    private double price;

    @Column(name = "discount")
    private boolean discount;
}
