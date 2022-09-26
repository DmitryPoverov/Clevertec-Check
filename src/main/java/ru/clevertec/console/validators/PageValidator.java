package ru.clevertec.console.validators;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class PageValidator {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_CARD_PAGE_SIZE = 2;
    private static final int DEFAULT_PRODUCT_PAGE_SIZE = 3;

    public static long checkAndReturnCardPageSize(long pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_CARD_PAGE_SIZE;
        }
        return pageSize;
    }

    public static long checkAndReturnProductPageSize(long pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_PRODUCT_PAGE_SIZE;
        }
        return pageSize;
    }

    public static long checkAndReturnPageNumber(long pageNumber) {
        if (pageNumber <= 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        return pageNumber;
    }

    public static double checkAndReturnMaxPageNumber(long pageSize, long allRows, double maxPageNumber) {
        if (allRows != 0) {
            maxPageNumber = (double) allRows / pageSize;
            if (maxPageNumber % 1 != 0) {
                maxPageNumber = (maxPageNumber - maxPageNumber % 1) + 1;
            }
        }
        return maxPageNumber;
    }

    public static boolean isSizeAndNumberAndRowNumberValid(long pageSize, long pageNumber, long allRows, double maxPageNumber) {
        return allRows != 0 || !(pageSize * pageNumber > maxPageNumber * pageSize);
    }
}
