package ru.clevertec.servlet;

import ru.clevertec.console.Check;
import ru.clevertec.console.serviceClass.CheckService;
import ru.clevertec.console.serviceClass.CheckServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/check")
public class CheckServlet extends HttpServlet {

    CheckService checkService = CheckServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String[]> parameterMap = req.getParameterMap();
        String[] ids = parameterMap.get("id");
        Check check = new Check(checkService, ids);
        List<String> stringsToPrint = check.getCheckService().createList(check);

        List<String> dottedStringToPrint = new ArrayList<>();
        for (String s : stringsToPrint) {
            dottedStringToPrint.add("<code>" + s.replace(" ", ".") + "</code>");
        }

        req.setAttribute("strings", dottedStringToPrint);
        req.getRequestDispatcher("/WEB-INF/jsp/check.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> args2 = new ArrayList<>();

        String id1 = req.getParameter("id1");
        String quantity1 = req.getParameter("quantity1");
        String id2 = req.getParameter("id2");
        String quantity2 = req.getParameter("quantity2");
        String id3 = req.getParameter("id3");
        String quantity3 = req.getParameter("quantity3");
        String discountCard = req.getParameter("discountCard");

        int counter=0;

        if (!id1.equals("") && !quantity1.equals("")) {
            args2.add(id1 + "-" + quantity1);
            counter++;
        }
        if (!id2.equals("") && !quantity2.equals("")) {
            args2.add(id2 + "-" + quantity2);
            counter++;
        }
        if (!id3.equals("") &&  !quantity3.equals("")) {
            args2.add(id3 + "-" + quantity3);
            counter++;
        }
        if (!discountCard.equals("")) {
            args2.add("card-" + discountCard);
            counter++;
        }

        String[] newArgs = new String[counter];
        for (int i=0, k=0; i<args2.size(); i++) {
            if (args2.get(i)!=null && !args2.get(i).equals("-")) {
                newArgs[k++] = args2.get(i);
            }
        }

        Check check = new Check(checkService, newArgs);
        List<String> stringList = check.getCheckService().createList(check);

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = resp.getWriter()) {
            for (String s : stringList) {
                writer.write("<p><code>" + s.replace(" " , ".") + "</code></p>");
            }
        }
    }
}
