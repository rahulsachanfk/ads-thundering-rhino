package com.flipkart.flap.thunderingrhino.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

@AllArgsConstructor
@Getter
@Setter
public class BucketStatInfoBias implements Comparable<BucketStatInfoBias> {
    private String bucketId;
    private String pageContext;
    private Integer impression;
    private Integer views;
    private Integer engagement;
    private Double actionRate;
    private Double ctr;
    private Double cpc;
    private Double revenue;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer startHour;

    @Override
    public int compareTo(BucketStatInfoBias bsi) {
        if(bsi==null)
            return 1;
        Calendar myDate = new GregorianCalendar(year, month, day, startHour,0);
        Calendar argDate = new GregorianCalendar(bsi.year, bsi.month, bsi.day, bsi.startHour,0);
        return myDate.compareTo(argDate);
    }


    public static class BucketInfoMapperBias implements ResultSetMapper<BucketStatInfoBias> {

        @Override
        public BucketStatInfoBias map(int i, ResultSet r, StatementContext statementContext) throws SQLException {

            Double actionRate = r.getInt("engagement") / Double.valueOf(r.getInt("views"));
            Double ctr = r.getInt("engagement") / Double.valueOf(r.getInt("impression"));
            Double cpc = r.getInt("revenue") / Double.valueOf(r.getInt("engagement"));

            return new BucketStatInfoBias(r.getString("bucket_id"),
                    "all",
                    r.getInt("impression"),
                    r.getInt("views"),
                    r.getInt("engagement"),
                    actionRate,
                    ctr,
                    cpc,
                    r.getDouble("revenue"),
                    r.getInt("year"),
                    r.getInt("month"),
                    r.getInt("day"),
                    r.getInt("startHour"));
        }
    }
}
