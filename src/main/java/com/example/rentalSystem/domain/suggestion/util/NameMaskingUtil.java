package com.example.rentalSystem.domain.suggestion.util;


public class NameMaskingUtil {

    public static String mask(String name) {
        if (name == null || name.isBlank()) return "*";
        int length = name.length();
        if (length == 1) return "*";
        if (length == 2) return name.charAt(0) + "*";
        return name.charAt(0) + "*" + name.charAt(length - 1);
    }
}
