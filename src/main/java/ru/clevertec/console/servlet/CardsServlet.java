package ru.clevertec.console.servlet;

import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.service.interfaces.DiscountCardService;
import ru.clevertec.console.utils.ContextUtil;

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

@WebServlet("/cards")
public class CardsServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private DiscountCardService<Integer, DiscountCard> service;

    @Override
    public void init() {
        ApplicationContext instance = ContextUtil.getInstance();
        service = instance.getBean("discountCardServiceImpl", DiscountCardService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json;
        String size = req.getParameter("size");
        String page = req.getParameter("page");
        try {
            List<DiscountCard> cards;
            if (size != null && page != null) {
                cards = service.findAll(Integer.parseInt(size), Integer.parseInt(page));
            } else {
                cards = service.findAll(0, 0);
            }
            json = gson.toJson(cards);
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
