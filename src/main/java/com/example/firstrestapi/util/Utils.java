package com.example.firstrestapi.util;


import java.util.List;

public class Utils {
    public static String buildIn(List<?> info){
        String sql = "(";
        for (Object o : info) {
            sql += o.toString() + ",";
        }
        return sql.substring(0, sql.length() - 1) + ")";
    }
}
