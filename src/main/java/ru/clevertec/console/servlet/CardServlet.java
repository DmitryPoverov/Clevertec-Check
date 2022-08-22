package ru.clevertec.console.servlet;

import com.google.gson.Gson;
import ru.clevertec.console.dao.implementations.DiscountCardDaoImpl;
import ru.clevertec.console.entities.DiscountCard;
import ru.clevertec.console.service.implementations.DiscountCardServiceImpl;
import ru.clevertec.console.service.interfaces.DiscountCardService;

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

@WebServlet("/cards/*")
public class CardServlet extends HttpServlet {

    private final DiscountCardService<Integer, DiscountCard> service = new DiscountCardServiceImpl(new DiscountCardDaoImpl());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = "";
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Optional<DiscountCard> maybeCard = service.findById(id);
            if (maybeCard.isPresent()) {
                DiscountCard card = maybeCard.get();
                json = gson.toJson(card);
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
