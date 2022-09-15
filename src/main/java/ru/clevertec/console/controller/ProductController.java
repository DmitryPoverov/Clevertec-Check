package ru.clevertec.console.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.service.interfaces.ProductService;

import java.util.List;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/id/{id}")
    public Product getProductById(@PathVariable long id) {
        return productService.findById(id).orElse(new Product());
    }

    @GetMapping("/name/{name}")
    public Product getProductByName(@PathVariable String name) {
        return productService.findByName(name).orElse(new Product());
    }

    @PostMapping("")
    public Product saveProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("")
    public Product updateProduct(Product product) {
        return productService.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id) {
        productService.deleteById(id);
    }
}
