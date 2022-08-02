package ru.clevertec.console.servlet;

import ru.clevertec.console.check.Check;
import ru.clevertec.console.serviceClass.CheckService;
import ru.clevertec.console.serviceClass.CheckServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = resp.getWriter()) {
            writer.write("<p><code>" + "File was written successfully." + "</code></p>");
            for (String s : stringsToPrint) {
                writer.write("<p><code>" + s.replace(" ", ".") + "</code></p>");
            }
            resp.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Enumeration<String> parameterNames = req.getParameterNames();
        Map<String, String[]> parameterMap = req.getParameterMap();

        String[] args = CHECK_SERVICE.getArgsList(parameterNames, parameterMap);

        Check check = new Check(CHECK_SERVICE, args);
        List<String> stringList = check.getCheckService().createList(check);

        CHECK_SERVICE.printToPDF(stringList);

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = resp.getWriter()) {
            writer.write("<p><code>" + "File was written." + "</code></p>");
            for (String s : stringList) {
                writer.write("<p><code>" + s.replace(" ", ".") + "</code></p>");
            }
            resp.setStatus(200);
        }
    }


}
