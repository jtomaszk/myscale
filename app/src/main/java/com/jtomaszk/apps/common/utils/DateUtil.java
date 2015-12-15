package com.jtomaszk.apps.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by jtomaszk on 15.12.15.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

    public static String dateToString(Date date) {
        return getShortLocalDateInstance().format(date);
    }

    // TODO: 15.12.15 write tests  for (Locale locale : Locale.getAvailableLocales())
    public static String dateToStringWithoutYears(Date date) {
        return getShortLocalDateInstanceWithoutYears().format(date);
    }

    public static long millisecondsToDays(long dateTimeMilliseconds) {
        return TimeUnit.MILLISECONDS.toDays(dateTimeMilliseconds);
    }

    public static Date millisecondsToDate(long dateTimeMilliseconds) {
        return new Date(dateTimeMilliseconds);
    }

    public static long daysToMilliseconds(long days) {
        return TimeUnit.DAYS.toMillis(days);
    }

    public static Date daysToDate(long days) {
        return new Date(daysToMilliseconds(days));
    }

    public static String millisecondsToShortString(long dateTimeMilliseconds) {
        Date date = millisecondsToDate(dateTimeMilliseconds);
        return dateToString(date);
    }

    public static String daysToShortString(long days) {
        Date date = daysToDate(days);
        return dateToString(date);
    }

    public static String daysToShortStringWithoutYears(long days) {
        Date date = daysToDate(days);
        return dateToStringWithoutYears(date);
    }

    private static DateFormat getShortLocalDateInstanceWithoutYears() {
        SimpleDateFormat sdf = (SimpleDateFormat) getShortLocalDateInstance();
        sdf.applyPattern(sdf.toPattern().replaceAll("[^\\p{Alpha}]*y+[^\\p{Alpha}]*", ""));
        return sdf;
    }

    private static DateFormat getShortLocalDateInstance() {
        return DateFormat.getDateInstance(DateFormat.SHORT);
    }
}
