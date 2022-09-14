package ru.clevertec.console.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.service.interfaces.DiscountCardService;

import java.util.List;

@RestController
@RequestMapping("/discount-cards")
@RequiredArgsConstructor
public class DiscountCardController {

    private final DiscountCardService discountCardService;

    @GetMapping
    public List<DiscountCard> getAllDiscountCards() {
        return discountCardService.findAll();
    }

    @GetMapping("/id/{id}")
    public DiscountCard getDiscountCardById(@PathVariable long id) {
        return discountCardService.findById(id).orElse(new DiscountCard());
    }

    @GetMapping("/number/{number}")
    public DiscountCard getDiscountCardByNumber(@PathVariable String number) {
        return discountCardService.findByNumber(number).orElse(new DiscountCard());
    }

    @PostMapping("")
    public DiscountCard addNewDiscountCard(@RequestBody DiscountCard employee) {
        return discountCardService.save(employee);
    }

    @PutMapping("")
    public DiscountCard updateDiscountCard(@RequestBody DiscountCard employee) {
        return discountCardService.save(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteDiscountCard(@PathVariable long id) {
        discountCardService.deleteById(id);
    }
}
