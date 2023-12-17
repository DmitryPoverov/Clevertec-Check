package ru.clevertec.console.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DaoUtil {

    public static long getNeededOffset(long pageSize, long pageNumber) {
        return pageSize * pageNumber - pageSize;
    }
}
