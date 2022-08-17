package ru.clevertec.console.validators;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexValidator {

    public static boolean isValid(String productString) {
        String regex = "^(100|[1-9]\\d?);([A-Z][a-z]{2,29}|[À-ß¨][à-ÿ¸]{2,29});(100\\.00|[1-9]\\d?\\.\\d{2});(20|1\\d|[1-9])$";
        return productString.matches(regex);
    }
}
