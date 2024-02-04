package com.nagarro.accountservice.utility;

import java.time.LocalDateTime;
import java.util.Random;

public class Utility {
    public static Long generateAccountNumber() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear() % 100; // Last two digits of the year
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millisecond = now.getNano() / 10_000_000; // Convert nanoseconds to milliseconds
        int random = new Random().nextInt(90) + 10; // Random number between 10 and 99
        return Long.parseLong(String.format("%02d%02d%02d%02d%02d%02d%02d%d", year, month, day, hour, minute, second, millisecond, random));
    }

}
