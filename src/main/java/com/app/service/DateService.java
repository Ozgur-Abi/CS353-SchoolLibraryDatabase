package com.app.service;

public class DateService {
    public static String dateString(Long longDate) {
        String date = Long.toString(longDate);
        return date.substring(6) + "." + date.substring(4, 6) + "." + date.substring(0, 4);
    }
}
