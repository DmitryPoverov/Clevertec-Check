package ru.clevertec.console.servlet;

import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.console.config.AppConfig;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.service.interfaces.DiscountCardService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/cards")
public class CardsServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private DiscountCardService service;

    @Override
    public void init() {
        ApplicationContext instance = new AnnotationConfigApplicationContext(AppConfig.class);
        service = instance.getBean("discountCardServiceImpl", DiscountCardService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<DiscountCard> cards = service.findAll();
        String json = gson.toJson(cards);
        resp.setContentType(MediaType.APPLICATION_JSON);
        PrintWriter writer = resp.getWriter();
        writer.write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
