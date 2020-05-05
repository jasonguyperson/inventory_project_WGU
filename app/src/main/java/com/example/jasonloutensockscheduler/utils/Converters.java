package com.example.jasonloutensockscheduler.utils;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class Converters {


    //utilities to convert LocalDate objects to 'text' (String) for SQLite.
    //SQLite does not support date objects/
    @TypeConverter
    public String localDateToString(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }

    @TypeConverter
    public LocalDate stringToLocalDate(String string) {
        if (string.trim().isEmpty()) {
            return null;
        } else {

            int year = Integer.parseInt(string.substring(0, 4));
            int month = Integer.parseInt(string.substring(5, 7));
            int day = Integer.parseInt(string.substring(8));

            LocalDate ld = LocalDate.of(year, month, day);

            return ld;
        }
    }

    public long localDateToMilliseconds(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        long timeInMilliseconds = instant.toEpochMilli();

        instant = localDate.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();

        timeInMilliseconds = instant.toEpochMilli();

        return timeInMilliseconds;
    }



}
