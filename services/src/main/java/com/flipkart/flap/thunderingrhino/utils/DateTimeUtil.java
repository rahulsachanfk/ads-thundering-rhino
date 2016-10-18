package com.flipkart.flap.thunderingrhino.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: anurag.laddha
 * Date: 09/09/14
 * Time: 7:58 AM
 * To change this template use File | Settings | File Templates.
 */


public class DateTimeUtil {

    public static final String FORMAT_DATETIME_MYSQL = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMEZONE_INDIA = "Asia/Kolkata";
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getEndOfHour(Date date, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getStartOfHour(Date date, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Difference between two dates in specified time unit. Order of date specified does not matter.
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    /**
     * Get hour of day from Date.
     * Value range is 0 to 23
     */
    public static int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getHourOfDay(Date date, TimeZone zone) {
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinuteOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getMinuteOfDate(Date date, TimeZone zone) {
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static Date addDaysToDate(Date date, int numDaysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numDaysToAdd);
        return calendar.getTime();
    }

    public static Date subtractDaysFromDate(Date date, int numDaysToSubtract) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -numDaysToSubtract);
        return calendar.getTime();
    }

    public static Date addMinutesToDate(Date date, int minutesToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutesToAdd);
        return calendar.getTime();
    }

    public static Date subtractMinutesFromDate(Date date, int numMinutesToSubtract) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -numMinutesToSubtract);
        return calendar.getTime();
    }

    public static Date addHoursToDate(Date date, int numHoursToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, numHoursToAdd);
        return calendar.getTime();
    }

    public static Date subtractHoursFromDate(Date date, int hoursToSubtract) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -hoursToSubtract);
        return calendar.getTime();
    }

    public static Date resetMilliSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Checks if date1 is equal or before date 2.
     *
     * @return true if date1 <= date2, else false
     */
    public static boolean equalOrBefore(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date1.getTime() <= date2.getTime();
        }
        return false;
    }

    /**
     * Checks if date1 is equal or greater then date2.
     *
     * @return true if date1>=date2, else false
     */
    public static boolean equalOrGreater(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date1.getTime() >= date2.getTime();
        }
        return false;
    }

    public static String toDateStr(Date date) {
        return  new SimpleDateFormat(FORMAT_DATETIME_MYSQL).format(date);
    }
}

