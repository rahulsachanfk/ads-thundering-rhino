package com.flipkart.flap.thunderingrhino.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ashishkumar.m
 * Date: 06/11/15
 * Time: 11:40 AM.
 */
@Data
@AllArgsConstructor
public class BucketStatInfo implements Comparable<BucketStatInfo> {
    private String bucketId;
    private String pageContext;
    private Integer impression;
    private Integer views;
    private Integer engagement;
    private Double avgPctr;
    private Double rankingScore;
    private Double actionRate;
    private Double ctr;
    private Double cpc;
    private Double revenue;
    private String bucketParams;
    private Integer weight;
    private Integer year;
    private Integer month;
    private Integer day;
    private int startHour;

    @Override
    public int compareTo(BucketStatInfo bsi) {
        if(bsi==null)
            return 1;
        Calendar myDate = new GregorianCalendar(year, month, day, startHour,0);
        Calendar argDate = new GregorianCalendar(bsi.year, bsi.month, bsi.day, bsi.startHour,0);
        return myDate.compareTo(argDate);
    }

    public static class BucketReportInfoMapper implements ResultSetMapper<BucketStatInfo> {

        @Override
        public BucketStatInfo map(int i, ResultSet r, StatementContext statementContext) throws SQLException {

            Double actionRate = r.getInt("engagement") / Double.valueOf(r.getInt("views"));
            Double ctr = r.getInt("engagement") / Double.valueOf(r.getInt("impression"));
            Double cpc = r.getInt("revenue") / Double.valueOf(r.getInt("engagement"));

            return new BucketStatInfo(r.getString("bucket_id"),
                    r.getString("pagecontext"),
                    r.getInt("impression"),
                    r.getInt("views"),
                    r.getInt("engagement"),
                    r.getDouble("avg_pctr"),
                    r.getDouble("ranking_score"),
                    actionRate,
                    ctr,
                    cpc,
                    r.getDouble("revenue"),
                    r.getString("bucket_params"),
                    r.getInt("weight"),
                    r.getInt("year"),
                    r.getInt("month"),
                    r.getInt("day"),
                    r.getInt("startHour"));
        }
    }
}
