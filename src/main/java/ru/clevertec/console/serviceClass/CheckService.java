package ru.clevertec.console.serviceClass;

import ru.clevertec.console.Check;
import ru.clevertec.console.CheckItem;

import java.io.IOException;
import java.util.List;

public interface CheckService {

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
