package com.gb.soa.sequence.util;

public class StringUtil {
    public static String sqlField(int length) {
        String field = "?";
        for (int i = 1; i < length; i++) {
            field = field + ",?";
        }
        return "values(" + field + ")";
    }
}

