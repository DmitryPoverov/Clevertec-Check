package ru.clevertec.console.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DaoUtility {

    public static int getNeededOffset(Integer pageSize, Integer pageNumber) {
        return pageSize * pageNumber - pageSize;
    }
}
