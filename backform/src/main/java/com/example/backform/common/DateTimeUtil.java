package com.example.backform.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String format(LocalDateTime t){ return t==null?null:t.format(F); }
}
