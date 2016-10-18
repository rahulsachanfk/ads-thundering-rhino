package com.flipkart.flap.thunderingrhino.utils;

import com.flipkart.flap.thunderingrhino.entities.BucketDailyStats;
import com.flipkart.flap.thunderingrhino.entities.BucketStatInfoBias;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;
import com.flipkart.flap.thunderingrhino.entities.BucketStatInfo;

import java.util.Collection;
import java.util.List;

/**
 * Created by ashishkumar.m
 * Date: 06/11/15
 * Time: 11:44 AM.
 */

@UseStringTemplate3StatementLocator
public interface BucketReportDAO {

    String tableName = "bucket_info";
    String tableDaily = "bucket_daily";
    String tableBias = "bucket_info_bias";

    @SqlUpdate("CREATE TABLE `bucket_info` (\n" +
            "  `bucket_id` varchar(40) DEFAULT NULL,\n" +
            "  `impression` bigint(20) DEFAULT NULL,\n" +
            "  `views` bigint(20) DEFAULT NULL,\n" +
            "  `engagement` bigint(20) DEFAULT NULL,\n" +
            "  `avg_pctr` double DEFAULT '0',\n" +
            "  `weight` int(11) DEFAULT NULL,\n" +
            "  `year` int(11) DEFAULT NULL,\n" +
            "  `month` int(11) DEFAULT NULL,\n" +
            "  `day` int(11) DEFAULT NULL,\n" +
            "  `startHour` int(11) DEFAULT NULL,\n" +
            "  `endHour` int(11) DEFAULT NULL,\n" +
            "  UNIQUE KEY `hour_Stat` (`bucket_id`,`year`,`month`,`day`,`startHour`)\n" +
            ")")
    void createBucketStatsTable();

    @RegisterMapper(BucketStatInfo.BucketReportInfoMapper.class)
    @SqlQuery("SELECT * FROM " + tableName + " WHERE year = :startYear " +
            " and month = :startMonth and day  \\>= :startDay ")
    public List<BucketStatInfo> getAllBucketInfoAfter(@Bind("startYear") Integer startYear, @Bind("startMonth") Integer startMonth, @Bind("startDay") Integer startDay);

    @RegisterMapper(BucketStatInfo.BucketReportInfoMapper.class)
    @SqlQuery("SELECT * FROM " + tableName + " WHERE year = :endYear " +
            " and month = :endMonth and day  \\<=:endDay ")
    public List<BucketStatInfo> getAllBucketInfoBefore(@Bind("endYear") Integer endYear, @Bind("endMonth") Integer endMonth, @Bind("endDay") Integer endDay);


    @RegisterMapper(BucketStatInfo.BucketReportInfoMapper.class)
    @SqlQuery("SELECT * FROM " + tableName + " WHERE year = :endYear " +
            " and month = :endMonth and day between :startDay and :endDay")
    public List<BucketStatInfo> getAllBucketInoByDateRange(@Bind("endYear") Integer endYear, @Bind("endMonth") Integer endMonth, @Bind("endDay") Integer endDay, @Bind("startDay") Integer startDay);

    @RegisterMapper(BucketStatInfo.BucketReportInfoMapper.class)
    @SqlQuery("SELECT * FROM " + tableName + " WHERE bucket_id in (<bucketIds>) and year = :startYear " +
            " and month = :startMonth and day  \\>= :startDay ")
    public List<BucketStatInfo> getBucketInfoAfter(@BindIn("bucketIds") List<String> bucketId, @Bind("startYear") Integer startYear, @Bind("startMonth") Integer startMonth, @Bind("startDay") Integer startDay);

    @RegisterMapper(BucketStatInfo.BucketReportInfoMapper.class)
    @SqlQuery("SELECT * FROM " + tableName + " WHERE bucket_id in (<bucketIds>) and year = :endYear " +
            " and month = :endMonth and day  \\<=:endDay ")
    public List<BucketStatInfo> getBucketInfoBefore(@BindIn("bucketIds") List<String> bucketId, @Bind("endYear") Integer endYear, @Bind("endMonth") Integer endMonth, @Bind("endDay") Integer endDay);

    @RegisterMapper(BucketStatInfo.BucketReportInfoMapper.class)
    @SqlQuery("SELECT * FROM " + tableName + " WHERE bucket_id in (<bucketIds>) and year = :endYear " +
            " and month = :endMonth and day between :startDay and :endDay ")
    public List<BucketStatInfo> getBucketInfoByDateRange(@BindIn("bucketIds") List<String> bucketIds, @Bind("endYear") Integer endYear, @Bind("endMonth") Integer endMonth, @Bind("endDay") Integer endDay, @Bind("startDay") Integer startDay);


    @RegisterMapper(BucketDailyStats.BucketDailyStatsMapper.class)
    @SqlQuery("select h.bucket_id, d.pageContext, d.channel, sum(h.engagement) as eng, sum(h.impression) as imp, d.conversions, d.auc, d.year, d.month, d.day from "+ tableDaily +" d join "+tableName+" h on " +
            "d.bucket_id = h.bucket_id and d.day=h.day where h.year=:year and h.month=:month and h.day \\>= :startDay and h.day \\<=:endDay and d.year=:year and d.month=:month and d.day \\>=:startDay and d.day \\<=:endDay group by d.bucket_id, d.day, d.pageContext, d.channel")
    public List<BucketDailyStats> getAllBucketDailyStats(@Bind("year") Integer endYear,@Bind("month") Integer endMonth,@Bind("endDay") Integer endDayofMonth,@Bind("startDay") Integer startDayOfMonth);

    @RegisterMapper(BucketDailyStats.BucketDailyStatsMapper.class)
    @SqlQuery("select h.bucket_id, d.pageContext, d.channel, sum(h.engagement) as eng, sum(h.impression) as imp, d.conversions, d.auc, d.year, d.month, d.day from "+ tableDaily +" d join "+tableName+" h on " +
            "d.bucket_id = h.bucket_id and d.day=h.day where h.year=:year and h.month=:month and h.day \\>= :startDay and d.year=:year and d.month=:month and d.day \\>=:startDay group by d.bucket_id, d.day, d.pageContext, d.channel")
    public List<BucketDailyStats> getAllBucketDailyStatsTillEOM(@Bind("year") Integer startYear,@Bind("month")  Integer startMonth,@Bind("startDay")  Integer startDay);

    @RegisterMapper(BucketDailyStats.BucketDailyStatsMapper.class)
    @SqlQuery("select h.bucket_id, d.pageContext, d.channel, sum(h.engagement) as eng, sum(h.impression) as imp, d.conversions, d.auc, d.year, d.month, d.day from "+ tableDaily +" d join "+tableName+" h on " +
            "d.bucket_id = h.bucket_id and d.day=h.day where h.year=:year and h.month=:month and h.day \\<=:endDay and d.year=:year and d.month=:month and d.day \\<=:endDay group by d.bucket_id, d.day, d.pageContext, d.channel")
    public List<BucketDailyStats> getAllBucketDailyStatsTillEndDay(@Bind("year")  Integer endYear,@Bind("month")  Integer endMonth,@Bind("endDay")  Integer endDay);

    @RegisterMapper(BucketDailyStats.BucketDailyStatsMapper.class)
    @SqlQuery("select h.bucket_id, d.pageContext, d.channel, sum(h.engagement) as eng, sum(h.impression) as imp, d.conversions, d.auc, d.year, d.month, d.day from "+ tableDaily +" d join "+tableName+" h on " +
            " d.bucket_id = h.bucket_id and d.day=h.day where h.year=:year and h.month=:month and h.day \\>=:startDay and d.year=:year and d.month=:month and d.day \\>=:startDay" +
            " and d.bucket_id in (<bucketIds>) and h.bucket_id in (<bucketIds>) group by d.bucket_id, d.day, d.pageContext, d.channel")
    public List<BucketDailyStats> getBucketDailyStatsTillEOM(@BindIn("bucketIds") List<String> bucketIds,@Bind("year") Integer startYear,@Bind("month") Integer startMonth,@Bind("startDay") Integer startDay);

    @RegisterMapper(BucketDailyStats.BucketDailyStatsMapper.class)
    @SqlQuery("select h.bucket_id, d.pageContext, d.channel, sum(h.engagement) as eng, sum(h.impression) as imp, d.conversions, d.auc, d.year, d.month, d.day from "+ tableDaily +" d join "+tableName+" h on " +
            " d.bucket_id = h.bucket_id and d.day=h.day where h.year=:year and h.month=:month and h.day \\<= :endDay and d.year=:year and d.month=:month and d.day \\<=:endDay" +
            "  and d.bucket_id in (<bucketIds>) and h.bucket_id in (<bucketIds>) group by d.bucket_id, d.day, d.pageContext, d.channel")
    public List<BucketDailyStats>  getBucketDailyStatsTillEndDay(@BindIn("bucketIds") List<String> bucketIds,@Bind("year") Integer endYear,@Bind("month") Integer endMonth,@Bind("endDay") Integer endDay);

    @RegisterMapper(BucketDailyStats.BucketDailyStatsMapper.class)
    @SqlQuery("select h.bucket_id, d.pageContext, d.channel, sum(h.engagement) as eng, sum(h.impression) as imp, d.conversions, d.auc, d.year, d.month, d.day from "+ tableDaily +" d join "+tableName+" h on " +
            "d.bucket_id = h.bucket_id and d.day=h.day where h.year=:year and h.month=:month and h.day \\>= :startDay and h.day \\<=:endDay and d.year=:year and d.month=:month and d.day \\>=:startDay and d.day \\<=:endDay" +
            " and d.bucket_id in (<bucketIds>) and h.bucket_id in (<bucketIds>) group by d.bucket_id, d.day, d.pageContext, d.channel")
    public List<BucketDailyStats>  getBucketDailyStats(@BindIn("bucketIds") List<String> bucketIds,@Bind("year") Integer endYear,@Bind("month") Integer endMonth,@Bind("endDay") Integer endDayofMonth,@Bind("startDay") Integer startDayOfMonth);

    @RegisterMapper(BucketStatInfoBias.BucketInfoMapperBias.class)
    @SqlQuery("SELECT * FROM " + tableBias + " WHERE year = :startYear " +
            " and month = :startMonth and day  \\>= :startDay ")
    public List<BucketStatInfoBias> getAllBucketInfoAfterBias(@Bind("startYear") Integer startYear, @Bind("startMonth") Integer startMonth, @Bind("startDay") Integer startDay);

    @RegisterMapper(BucketStatInfoBias.BucketInfoMapperBias.class)
    @SqlQuery("SELECT * FROM " + tableBias + " WHERE year = :endYear " +
            " and month = :endMonth and day  \\<=:endDay ")
    public List<BucketStatInfoBias> getAllBucketInfoBeforeBias(@Bind("endYear") Integer endYear, @Bind("endMonth") Integer endMonth, @Bind("endDay") Integer endDay);


    @RegisterMapper(BucketStatInfoBias.BucketInfoMapperBias.class)
    @SqlQuery("SELECT * FROM " + tableBias + " WHERE year = :endYear " +
            " and month = :endMonth and day between :startDay and :endDay")
    public List<BucketStatInfoBias> getAllBucketInoByDateRangeBias(@Bind("endYear") Integer endYear, @Bind("endMonth") Integer endMonth, @Bind("endDay") Integer endDay, @Bind("startDay") Integer startDay);

    @RegisterMapper(BucketStatInfoBias.BucketInfoMapperBias.class)
    @SqlQuery("SELECT * FROM " + tableBias + " WHERE bucket_id in (<bucketIds>) and year = :startYear " +
            " and month = :startMonth and day  \\>= :startDay ")
    public List<BucketStatInfoBias> getBucketInfoAfterBias(@BindIn("bucketIds") List<String> bucketId, @Bind("startYear") Integer startYear, @Bind("startMonth") Integer startMonth, @Bind("startDay") Integer startDay);

    @RegisterMapper(BucketStatInfoBias.BucketInfoMapperBias.class)
    @SqlQuery("SELECT * FROM " + tableBias + " WHERE bucket_id in (<bucketIds>) and year = :endYear " +
            " and month = :endMonth and day  \\<=:endDay ")
    public List<BucketStatInfoBias> getBucketInfoBeforeBias(@BindIn("bucketIds") List<String> bucketId, @Bind("endYear") Integer endYear, @Bind("endMonth") Integer endMonth, @Bind("endDay") Integer endDay);

    @RegisterMapper(BucketStatInfoBias.BucketInfoMapperBias.class)
    @SqlQuery("SELECT * FROM " + tableBias + " WHERE bucket_id in (<bucketIds>) and year = :endYear " +
            " and month = :endMonth and day between :startDay and :endDay ")
    public List<BucketStatInfoBias> getBucketInfoByDateRangeBias(@BindIn("bucketIds") List<String> bucketIds, @Bind("endYear") Integer endYear, @Bind("endMonth") Integer endMonth, @Bind("endDay") Integer endDay, @Bind("startDay") Integer startDay);
}
