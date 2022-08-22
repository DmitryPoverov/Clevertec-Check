package ru.clevertec.console.servlet;

import com.google.gson.GsonBuilder;
import ru.clevertec.console.dao.implementations.DiscountCardDaoImpl;
import ru.clevertec.console.dao.implementations.ProductDaoImpl;
import ru.clevertec.console.entities.Check;
import ru.clevertec.console.service.implementations.CheckServiceImpl;
import ru.clevertec.console.service.implementations.DiscountCardServiceImpl;
import ru.clevertec.console.service.implementations.ProductServiceImpl;
import ru.clevertec.console.service.interfaces.CheckService;
import ru.clevertec.console.utils.PrintUtil;
import ru.clevertec.console.utils.ServletUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/check")
@SuppressWarnings("Duplicates")
public class CheckServlet extends HttpServlet {

    private final CheckService<String , Check> service = new CheckServiceImpl(
            new DiscountCardServiceImpl(new DiscountCardDaoImpl()),
            new ProductServiceImpl(new ProductDaoImpl()));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String[] args = ServletUtil.getArgArrayFromRequestWithId(req);
        List<String> stringsToPrint = service.handleArrayAndGetStrungList(args);

        PrintUtil.printToPDF(stringsToPrint);

        resp.setContentType("application/json");
        try (PrintWriter writer = resp.getWriter()) {
            String s = new GsonBuilder().setPrettyPrinting().create().toJson(stringsToPrint);
            writer.write(s);
            resp.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String[] args = ServletUtil.getArgArrayFromRequestParameters(req);
        List<String> stringsToPrint = service.handleArrayAndGetStrungList(args);

        PrintUtil.printToPDF(stringsToPrint);

        resp.setContentType("application/json");
        try (PrintWriter writer = resp.getWriter()) {
            String s = new GsonBuilder().setPrettyPrinting().create().toJson(stringsToPrint);
            writer.write(s);
            resp.setStatus(200);
        }
    }


}
