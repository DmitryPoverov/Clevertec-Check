package ru.clevertec.console.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.console.dto.ProductDto;
import ru.clevertec.console.service.interfaces.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable long id) {
        return productService.findById(id).orElse(new ProductDto());
    }

    @GetMapping("/name/{name}")
    public ProductDto getProductByName(@PathVariable String name) {
        return productService.findByName(name).orElse(new ProductDto());
    }

    @PostMapping("")
    public ProductDto saveProduct(@RequestBody ProductDto product) {
        return productService.save(product);
    }

    @PutMapping("")
    public ProductDto updateProduct(@RequestBody ProductDto product) {
        return productService.update(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id) {
        productService.deleteById(id);
    }
}
