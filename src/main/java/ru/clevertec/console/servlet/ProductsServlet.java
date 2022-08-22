package ru.clevertec.console.servlet;

import com.google.gson.Gson;
import ru.clevertec.console.dao.implementations.ProductDaoImpl;
import ru.clevertec.console.entities.Product;
import ru.clevertec.console.service.implementations.ProductServiceImpl;
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
import java.util.List;

@WebServlet("/products")
public class ProductsServlet extends HttpServlet {

    private static final ProductService<Integer, Product> SERVICE = new ProductServiceImpl(new ProductDaoImpl());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Product> products = SERVICE.findAll(5, 1);
            String json = gson.toJson(products);
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
