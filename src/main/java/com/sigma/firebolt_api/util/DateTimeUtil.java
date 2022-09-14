package com.sigma.firebolt_api.util;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static LocalDateTime getMidnight(LocalDateTime localDateTime) {
        int dayOfMonth = localDateTime.getDayOfMonth();

        int monthValue = localDateTime.getMonthValue();
        String month = "";
        if (monthValue < 10) {
            month = "0";
        }
        month += monthValue;

        int year = localDateTime.getYear();

        return dateFromString(dayOfMonth + "/" + month + "/" + year + " 23:59:59");
    }

    public static LocalDateTime dateFromString(String stringDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.parse(stringDate, dateTimeFormatter);
    }

    public static long toMillis(LocalDateTime localDateTime) {
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    public static long getDifference(long initialDate, long finalDate) {
        return finalDate - initialDate;
    }
}
