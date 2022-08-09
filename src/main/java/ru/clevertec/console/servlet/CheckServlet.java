package ru.clevertec.console.servlet;

import com.google.gson.GsonBuilder;
import ru.clevertec.console.check.Check;
import ru.clevertec.console.serviceClass.CheckService;
import ru.clevertec.console.serviceClass.CheckServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@WebServlet("/check")
public class CheckServlet extends HttpServlet {

    private static final CheckService CHECK_SERVICE = CheckServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Map<String, String[]> parameterMap = req.getParameterMap();
        String[] args = parameterMap.get("id");
        Check check = new Check(CHECK_SERVICE, args);
        List<String> stringsToPrint = check.getCheckService().createList(check);

        CHECK_SERVICE.printToPDF(stringsToPrint);

        resp.setContentType("application/json");
        try (PrintWriter writer = resp.getWriter()) {
            String s = new GsonBuilder().setPrettyPrinting().create().toJson(stringsToPrint);
            writer.write(s);
            resp.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Enumeration<String> parameterNames = req.getParameterNames();
        Map<String, String[]> parameterMap = req.getParameterMap();

        String[] args = CHECK_SERVICE.getArgArray(parameterNames, parameterMap);

        Check check = new Check(CHECK_SERVICE, args);
        List<String> stringsToPrint = check.getCheckService().createList(check);

        CHECK_SERVICE.printToPDF(stringsToPrint);

        resp.setContentType("application/json");
        try (PrintWriter writer = resp.getWriter()) {
            String s = new GsonBuilder().setPrettyPrinting().create().toJson(stringsToPrint);
            writer.write(s);
            resp.setStatus(200);
        }
    }


}
