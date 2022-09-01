package ru.clevertec.console.servlet;

import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.console.config.AppConfig;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.service.interfaces.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private ProductService<Integer, Product> service;

    @Override
    public void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean("productServiceImpl", ProductService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = "";
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Optional<Product> maybeProduct = service.findById(id);
            if (maybeProduct.isPresent()) {
                Product product = maybeProduct.get();
                json = gson.toJson(product);
            }
            resp.setContentType(MediaType.APPLICATION_JSON);
            PrintWriter writer = resp.getWriter();
            writer.write(json);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
