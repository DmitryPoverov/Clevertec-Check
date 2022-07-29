package ru.clevertec.console;

import java.io.IOException;

public class CheckRunner {

    public static void main(String[] args) throws IOException {

        if (args[0].equals("--f")) {
            String path = args[1];
            Check check = new Check(path);
            check.printToFile(args[2]);
            System.out.println("!!! File was written !!!");
        } else if (args[0].equals("--s")) {
            Check check = new Check();
            String path = args[1];
            String contentOfFile = check.convertPathStringToTextString(path, "\r\n");
            String[] inputStrings = check.convertStringToArray(contentOfFile, "\r\n");
            check.checkData(inputStrings, args[2]);
            check.printToConsoleFromFile();
        } else {
            Check check = new Check(args);
            for (String s : check.printToStringList()) {
                System.out.println(s);
            }
        }
    }
}
