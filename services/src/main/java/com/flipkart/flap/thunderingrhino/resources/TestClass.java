package com.flipkart.flap.thunderingrhino.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by pavan.t on 30/09/15.
 */
public class TestClass {

    public long getYesterday() {

        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar c = Calendar.getInstance(timeZone);

        c.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println(c.getTime());
        return c.getTimeInMillis();
    }


    private long getToday() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        Date date = new Date();
        String fomratted = sdf.format(date);
        Date fmdate = sdf.parse(fomratted);
        System.out.printf(fmdate.toString());
        return fmdate.getTime();
    }
    public static void main(String[] args) throws ParseException {
        TestClass testClass = new TestClass();
        System.out.println(testClass.getYesterday());
        System.out.println(testClass.getToday());
    }
}
