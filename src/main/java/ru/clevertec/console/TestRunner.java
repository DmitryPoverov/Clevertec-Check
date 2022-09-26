package ru.clevertec.console;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.console.config.AppConfig;
import ru.clevertec.console.service.interfaces.CheckService;
import ru.clevertec.console.service.interfaces.DiscountCardService;
import ru.clevertec.console.service.interfaces.ProductService;

public class TestRunner {

    public static void main(String[] args) {


        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        CheckService checkService = context.getBean("checkServiceImpl", CheckService.class);
        DiscountCardService discountCardService = context.getBean("discountCardServiceImpl", DiscountCardService.class);
        ProductService productService = context.getBean("productServiceImpl", ProductService.class);

// DiscountCardService
/* check findAll() *
        List<DiscountCard> all = discountCardService.findAll();
        all.forEach(System.out::println);*/

/* check findById() *
        Optional<DiscountCard> byId = discountCardService.findById(1);
        System.out.println(byId);*/

/* check findByNumber() *
        Optional<DiscountCard> byNumber = discountCardService.findByNumber("card-120");
        System.out.println(byNumber);*/

/* check save(DiscountCard card)
        DiscountCard card = discountCardService.save(DiscountCard.builder().number("card-999").build());
        System.out.println(card.getId());*/

/* check delete(long id)
        System.out.println(discountCardService.findByNumber("card-999"));
        discountCardService.deleteById(82);
        System.out.println(discountCardService.findByNumber("card-999"));*/

/* check findById(), update(), save() *
        Optional<DiscountCard> byId = discountCardService.findById(5);
        System.out.println("1************" + byId);

        DiscountCard card = DiscountCard.builder().id(5).number("card-999").build();
        discountCardService.update(card);
        Optional<DiscountCard> byId2 = discountCardService.findById(5);
        System.out.println("2************" + byId2);

        DiscountCard card1 = DiscountCard.builder().id(5).number("card-777").build();
        discountCardService.update(card1);
        Optional<DiscountCard> byId3 = discountCardService.findById(5);
        System.out.println("3************" + byId3);

        DiscountCard save = discountCardService.save(DiscountCard.builder().number("card-777").build());
        System.out.println("4************" + save);*/

// ProductService
/* check findAll() *
        List<Product> all = productService.findAll();
        all.forEach(System.out::println);*/

/* check findById() *
        Optional<Product> byId = productService.findById(1);
        System.out.println(byId);*/

/* check findByNumber() *
        Optional<Product> byNumber = productService.findByName("Dress1_db");
        System.out.println(byNumber);*/

/* check save(DiscountCard card), delete(long id) *
        Product product = productService.save(Product.builder().title("test").price(1.01).discount(true).build());
        long id = product.getId();
        System.out.println(productService.findByName("test"));
        productService.deleteById(id);
        System.out.println(productService.findByName("test"));*/

/* check findById(), update() *
        Optional<Product> byId = productService.findById(1);
        System.out.println("1************" + byId);

        Product product = Product.builder().id(1).title("test").price(1.01).discount(true).build();
        productService.update(product);
        byId = productService.findById(1);
        System.out.println("2************" + byId);

        product = Product.builder().id(1).title("Dress1_db").price(10.11).discount(false).build();
        productService.update(product);
        byId = productService.findById(5);
        System.out.println("3************" + byId);*/

    }
}
