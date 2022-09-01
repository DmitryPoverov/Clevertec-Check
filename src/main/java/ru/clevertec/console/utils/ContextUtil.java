package ru.clevertec.console.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.console.config.AppConfig;

public class ContextUtil {

    private static final ApplicationContext CONTEXT = new AnnotationConfigApplicationContext(AppConfig.class);

    public static ApplicationContext getInstance() {
        return CONTEXT;
    }
}
