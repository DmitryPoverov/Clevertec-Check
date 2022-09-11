package ru.clevertec.console.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.experimental.UtilityClass;
import ru.clevertec.console.entities.Check;
import ru.clevertec.console.entities.Product;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@UtilityClass
public class PrintUtil {

    private static final String FILE_PATH = "/pdfCheck.pdf";

    public static void printToPDF(List<String> list) {
        Document document = new Document();
        try {
            iTextHandler(list, document);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
    }

    public static File printToPDFAndReturn(List<String> list) {
        Document document = new Document();
        try {
            iTextHandler(list, document);
            return new File(FILE_PATH);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }
    }

    private static void iTextHandler(List<String> list, Document document) throws DocumentException, FileNotFoundException {
        PdfWriter.getInstance(document, new FileOutputStream(FILE_PATH));
        Font font = new Font();
        Rectangle a5 = PageSize.A5;
        font.setFamily("Courier");
        document.setPageSize(a5);
        document.open();
        for (String s : list) {
            document.add(new Paragraph(s, font));
        }
    }

    public void printToConsoleFromFile(Check check) {
        System.out.println("--------------------------------------");
        System.out.println("            CASH RECEIPT");
        System.out.println("            Supermarket\n");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("                  " + formatter.format(new Date()));
        System.out.println("--------------------------------------");
        System.out.println("QTY DESCRIPTION         PRICE   TOTAL");
        double finalPrice = 0;
        for (Product product : check.getCheckItemMap().keySet()) {

            String description = product.getTitle();
            int quantity = check.getCheckItemMap().get(product);
            double price = product.getPrice();
            double total = quantity * price;
            finalPrice += total;

            System.out.printf("%2d  %-17s %7.2f  %6.2f%n", quantity, description, price, total);
        }
        System.out.println("--------------------------------------");
        System.out.printf("TOTAL %31.2f%n", finalPrice);
        System.out.println("--------------------------------------");
    }

    public void printToFile(List<String> stringList, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String s : stringList) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("!!! You entered a wrong path!!! ");
        }
    }

}
