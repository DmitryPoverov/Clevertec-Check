package ru.clevertec.console.proxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.console.Check;
import ru.clevertec.console.serviceClass.CheckService;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ServiceHandler implements InvocationHandler {

    private static final Logger LOGGER = LogManager.getLogger();
    private final CheckService checkService;

    public ServiceHandler(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Gson parser = new GsonBuilder().setPrettyPrinting().create();

        if (args == null) {
            LOGGER.info("args = null");
        } else {
            Arrays.stream(args).forEach(arg -> {
                if ("String[]".equals(arg.getClass().getSimpleName())) {
                    LOGGER.info(method.getName() + " / " + Arrays.toString((String[])arg));
                } else {
                    LOGGER.info(method.getName() + " / " + arg);
                }
            });
        }
        if ("printToConsoleFromFile".equals(method.getName()) ||
                "printToStringList".equals(method.getName()) ||
                "printToFile".equals(method.getName())) {
            File file = new File("src/main/resources/checkFile.json");
            try (FileWriter writer = new FileWriter(file, false)) {
                if (args != null && args[0] != null) {
                    Check check = (Check) args[0];
                    String object = parser.toJson(check);
                    writer.write(object);
                }
            }
        }
        return method.invoke(checkService, args);
    }
}
