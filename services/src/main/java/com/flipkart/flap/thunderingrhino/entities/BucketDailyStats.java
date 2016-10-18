package com.flipkart.flap.thunderingrhino.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Data
@AllArgsConstructor
public class BucketDailyStats implements Comparable<BucketDailyStats>{
    private String bucketId;
    private String pageContext;
    private String channel;
    private Double cvr;
    private Double cvi;
    private Integer conversion;
    private Integer engagements;
    private Integer impressions;
    private Double auc;
    private Integer year;
    private Integer month;
    private Integer day;

    @Override
    public int compareTo(BucketDailyStats bds) {
        if(bds==null)
            return 1;
        Calendar myDate = new GregorianCalendar(year, month, day, 0, 0);
        Calendar argDate = new GregorianCalendar(bds.year, bds.month, bds.day, 0, 0);
        return myDate.compareTo(argDate);
    }

    public static class BucketDailyStatsMapper implements ResultSetMapper<BucketDailyStats> {

        @Override
        public BucketDailyStats map(int i, ResultSet r, StatementContext statementContext) throws SQLException {

            Double cvr = r.getInt("conversions") / Double.valueOf(r.getInt("eng"));
            Double cvi = r.getInt("conversions") / Double.valueOf(r.getInt("imp"));
            String pageContext = r.getString("pagecontext") == null ? "all" : r.getString("pagecontext");
            String channel = r.getString("channel") == null ? "mapp" : r.getString("channel");

            return new BucketDailyStats(r.getString("bucket_id"),
                    pageContext,
                    channel,
                    cvr,
                    cvi,
                    r.getInt("conversions"),
                    r.getInt("eng"),
                    r.getInt("imp"),
                    r.getDouble("auc"),
                    r.getInt("year"),
                    r.getInt("month"),
                    r.getInt("day")
            );
        }
    }
}
