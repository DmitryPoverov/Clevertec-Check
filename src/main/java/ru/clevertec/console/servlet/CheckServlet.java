package ru.clevertec.console.servlet;

import com.google.gson.GsonBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.console.config.AppConfig;
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

    private CheckService service;

    @Override
    public void init() {
        ApplicationContext instance = new AnnotationConfigApplicationContext(AppConfig.class);
        service = instance.getBean("checkServiceImpl", CheckService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String[] args = ServletUtil.getArgArrayFromRequestWithId(req);
        List<String> stringsToPrint = service.handleArrayAndGetStrungList(args);

        PrintUtil.printToPDF(stringsToPrint);

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

        try (PrintWriter writer = resp.getWriter()) {
            String s = new GsonBuilder().setPrettyPrinting().create().toJson(stringsToPrint);
            writer.write(s);
            resp.setStatus(200);
        }
    }


}
