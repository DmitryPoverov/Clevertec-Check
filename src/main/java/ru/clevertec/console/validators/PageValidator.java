package ru.clevertec.console.validators;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class PageValidator {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PRODUCT_PAGE_SIZE = 3;
    private static final int DEFAULT_CARD_PAGE_SIZE = 2;

    public static int checkAndReturnProductPageSize(int pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_PRODUCT_PAGE_SIZE;
        }
        return pageSize;
    }

    public static int checkAndReturnCardPageSize(int pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_CARD_PAGE_SIZE;
        }
        return pageSize;
    }

    public static int checkAndReturnPageNumber(int pageNumber) {
        if (pageNumber <= 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        return pageNumber;
    }

    public static double checkAndReturnMaxPageNumber(int pageSize, int allRows, double maxPageNumber) {
        if (allRows != 0) {
            maxPageNumber = (double) allRows / pageSize;
            if (maxPageNumber % 1 != 0) {
                maxPageNumber = (maxPageNumber - maxPageNumber % 1) + 1;
            }
        }
        return maxPageNumber;
    }

    public static boolean isSizeAndNumberAndRowNumberValid(int pageSize, int pageNumber, int allRows, double maxPageNumber) {
        return allRows != 0 || !(pageSize * pageNumber > maxPageNumber * pageSize);
    }
}
