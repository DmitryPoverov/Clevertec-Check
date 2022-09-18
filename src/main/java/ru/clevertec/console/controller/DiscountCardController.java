package ru.clevertec.console.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.console.dto.DiscountCardDto;
import ru.clevertec.console.service.interfaces.DiscountCardService;

import java.util.List;

@RestController
@RequestMapping("/discount-cards")
@RequiredArgsConstructor
public class DiscountCardController {

    private final DiscountCardService discountCardService;

    @GetMapping
    public List<DiscountCardDto> getAllDiscountCards() {
        return discountCardService.findAll();
    }

    @GetMapping("/{id}")
    public DiscountCardDto getDiscountCardById(@PathVariable long id) {
        return discountCardService.findById(id).orElse(new DiscountCardDto());
    }

    @GetMapping("/number/{number}")
    public DiscountCardDto getDiscountCardByNumber(@PathVariable String number) {
        return discountCardService.findByNumber(number).orElse(new DiscountCardDto());
    }

    @PostMapping("")
    public DiscountCardDto addNewDiscountCard(@RequestBody DiscountCardDto employee) {
        return discountCardService.save(employee);
    }

    @PutMapping("")
    public DiscountCardDto updateDiscountCard(@RequestBody DiscountCardDto employee) {
        return discountCardService.save(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteDiscountCard(@PathVariable long id) {
        discountCardService.deleteById(id);
    }
}
