package ru.clevertec.console.utils;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ServletUtil {

    public String[] getArgArrayFromRequestWithId(HttpServletRequest req) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        return parameterMap.get("id");
    }

    public static String[] getArgArrayFromRequestParameters(HttpServletRequest req) {
        Enumeration<String> parameterNames = req.getParameterNames();
        Map<String, String[]> parameterMap = req.getParameterMap();
        List<String> methodArgs = new ArrayList<>();
        while (parameterNames.hasMoreElements()) {
            String s = parameterNames.nextElement();
            StringBuilder builder = new StringBuilder();
            if (s.contains("id")) {
                String[] id = parameterMap.get(s);
                if (!"".equals(id[0])) {
                    builder.append(id[0]).append("-");
                }
                if (parameterNames.hasMoreElements()) {
                    s = parameterNames.nextElement();
                    if (s.contains("quantity")) {
                        String[] quantity = parameterMap.get(s);
                        for (String s2 : quantity) {
                            if (!"".equals(s2)) {
                                builder.append(s2);
                            }
                        }
                    }
                }
            } else if (s.contains("discount")) {
                String[] discount = parameterMap.get(s);
                builder.append("card-").append(discount[0]);
            }
            if (!"".equals(builder.toString())) {
                methodArgs.add(builder.toString());
            }
        }
        return methodArgs.toArray(new String[0]);
    }
}
