package ru.clevertec.console;

import ru.clevertec.console.proxy.ServiceHandler;
import ru.clevertec.console.serviceClass.CheckService;
import ru.clevertec.console.serviceClass.CheckServiceImpl;

import java.io.IOException;
import java.lang.reflect.Proxy;

public class CheckRunner {

    public static void main(String[] args) throws IOException {

        CheckService checkService;
        CheckService tempService = CheckServiceImpl.getInstance();
        ClassLoader classLoader = tempService.getClass().getClassLoader();
        Class<?>[] interfaces = tempService.getClass().getInterfaces();
        checkService = (CheckService) Proxy.newProxyInstance(classLoader, interfaces, new ServiceHandler(tempService));

        if (args[0].equals("--f")) {
            String path = args[1];
            Check check = new Check(checkService, path);
            check.getCheckService().printToFile(check, args[2]);
            System.out.println("File is written");
        } else if (args[0].equals("--s")) {
            Check check = new Check(checkService);
            String path = args[1];
            String contentOfFile = check.getCheckService().convertPathStringToTextString(path, "\r\n");
            String[] inputStrings = check.getCheckService().convertStringToArray(contentOfFile, "\r\n");
            check.getCheckService().checkData(inputStrings, args[2], check);
            check.getCheckService().printToConsoleFromFile(check);
        } else {
            Check check = new Check(checkService, args);
            for (String s : check.getCheckService().printToStringList(check)) {
                System.out.println(s);
            }
        }
    }
}
