package ru.clevertec.console.serviceClass;

import java.util.Arrays;
import java.util.List;

public class PDFRunner {

    public static void main(String[] args) {

        CheckService instance = CheckServiceImpl.getInstance();

        List<String> strings = Arrays.asList("00000", "88888");

        instance.printToPDF(strings);
    }
}
