package com.jtomaszk.apps.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static Date millisecondsToDate(long dateTimeMilliseconds) {
        return new Date(dateTimeMilliseconds);
    }

    public static long daysToMilliseconds(long days) {
        return TimeUnit.DAYS.toMillis(days);
    }

    public static Date daysToDate(long days) {
        return new Date(daysToMilliseconds(days));
    }

    public static Calendar millisecondsToCalendar(long milliseconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        return cal;
    }

    public static int millisecondsToWeek(long days) {
        Calendar cal = millisecondsToCalendar(days);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int millisecondsToMonth(long days) {
        Calendar cal = millisecondsToCalendar(days);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static String millisecondsToShortString(long dateTimeMilliseconds) {
        Date date = millisecondsToDate(dateTimeMilliseconds);
        return dateToString(date);
    }

    public static String millisecondsToShortStringWithoutYears(long days) {
        Date date = millisecondsToDate(days);
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

    public static Long truncateMillisecondsToMonth(long milliseconds) {
        Calendar cal = millisecondsToCalendar(milliseconds);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long truncateMillisecondsToWeek(long milliseconds) {
        Calendar cal = millisecondsToCalendar(milliseconds);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static Long truncateMillisecondsToDay(long milliseconds) {
        Calendar cal = millisecondsToCalendar(milliseconds);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static DateFormat getDateInstance() {
        return DateFormat.getDateInstance();
    }

    public static DateFormat getTimeInstance() {
        return DateFormat.getTimeInstance();
    }

    public static DateFormat getDateTimeInstance() {
        return DateFormat.getDateTimeInstance();
    }
}
