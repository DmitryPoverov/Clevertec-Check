package ru.clevertec.console.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "number")
@Setter
@Getter
@ToString
public class DiscountCardDto {

    private long id;
    private String number;
}
