package ru.clevertec.console.serviceClass;

import ru.clevertec.console.check.Check;
import ru.clevertec.console.dto.CheckItem;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public interface CheckService {

    String[] getArgsList(Enumeration<String> parameterNames, Map<String, String[]> parameterMap);
    void printToPDF(List<String> list);
    void parseParamsToGoodsAndCard(String[] arguments, Check check);
    void checkData(String[] strings, String invalidDataFilePath, Check check);
    List<String> createList(Check check);
    void printToFile(Check check, String path);
    List<String> printToStringList(Check check);
    boolean isValid(String productString);
    String[] convertStringToArray(String text, String regex);
    String convertPathStringToTextString(String path, String delimiter) throws IOException;
    List<CheckItem> setParamMapper(List<String> params, String regex);
    void printToConsoleFromFile(Check check);
}
