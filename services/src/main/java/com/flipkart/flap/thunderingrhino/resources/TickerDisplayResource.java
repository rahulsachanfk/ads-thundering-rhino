package com.flipkart.flap.thunderingrhino.resources;

import com.codahale.metrics.annotation.Timed;
import org.skife.jdbi.v2.Folder2;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.util.StringMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pavan.t on 28/09/15.
 */

@Path("/services/ticker")

public class TickerDisplayResource {


    Handle handle;
    Handle anyhandle;
    private JedisPool jedisPool;

    public TickerDisplayResource(Handle handle,Handle anyhandle,JedisPool jedis) {
        this.handle = handle;
        this.jedisPool=jedis;
        this.anyhandle=anyhandle;
    }


    @Produces("application/html")
    @GET
    @Path("/engagements")
    @Timed
    public Response getEngagements() throws ClassNotFoundException, ParseException {

        int engagements=0;

        Jedis jedis=jedisPool.getResource();
        String date = jedis.get("lastdailyupdated");

        String last_24_hour_engagements = handle.createQuery("select sum(count) as engagements from data_stats_engagements_realtime_v2 where timeRange >=:today")
                .bind("today", getToday(date))
                .map(StringMapper.FIRST)
                .first();

        if(last_24_hour_engagements == null){
            last_24_hour_engagements="0";
        }

        engagements = Integer.parseInt(jedis.get("daily")) + Integer.parseInt(last_24_hour_engagements);




        String clicks=NumberFormat.getInstance(Locale.US).format(engagements);

        jedis.close();

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(clicks)
                .build();

    }


    @Produces("application/html")
    @GET
    @Path("/last_24hrs_engagements")
    @Timed
    public Response last_24_hrs_engagements(){

        String last_24_hour_engagements = handle.createQuery("select sum(count) as engagements from data_stats_engagements_realtime_v2 where timeRange >= :yesterday")
                .bind("yesterday", getYesterday())
                .map(StringMapper.FIRST)
                .first();

        String l24eng=NumberFormat.getInstance(Locale.US).format(Integer.parseInt(last_24_hour_engagements));


        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(l24eng)
                .build();
    }

    @Produces("application/json")
    @GET
    @Path("/plaactionrateinterval")
    @Timed
    public Response actionRateInterval(@QueryParam("strTime") String strTime,@QueryParam("enTime") String enTime) throws ClassNotFoundException, ParseException {

        String actRateIntervalQuery= "select `interval_start`, (sum(`ad_engagements`)/sum(`ad_views`))*100 ActionRate from `data_report_summary_stats` where `interval_start` between :strTime and :enTime group by `interval_start`;";
        LinkedHashMap<String,Double> actionRateMap = anyhandle.createQuery(actRateIntervalQuery).bind("strTime",strTime).bind("enTime", enTime).fold(new LinkedHashMap<String, Double>(), new Folder2<LinkedHashMap<String, Double>>() {
            @Override
            public LinkedHashMap<String, Double> fold(LinkedHashMap<String, Double> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
                accumulator.put(rs.getString("interval_start"), rs.getDouble("ActionRate"));
                return accumulator;
            }
        });
        System.out.println(actionRateMap);
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(actionRateMap).build();

    }

    @Produces("application/json")
    @GET
    @Path("/conversionrateinterval")
    @Timed
    public Response conversionRateInterval(@QueryParam("strTime") String strTime,@QueryParam("enTime") String enTime) throws ClassNotFoundException, ParseException {

        String actRateIntervalQuery ="select p.`timesta` timeRange , (p.`cnv`/t.`act`)*100 cnvrate  from (select sum(`count`) act, DATE_FORMAT(from_unixtime(floor(`timeRange`/1000)),'%Y-%m-%d %H:00:00') as timeRange1 from `data_stats_engagements_realtime_v2`  where from_unixtime(floor(`timeRange`/1000)) BETWEEN :strTime and :enTime GROUP BY `timeRange1`) t left join (select  sum(`quantity`) cnv, DATE_FORMAT(`conversion_time`, '%Y-%m-%d %H:00:00') AS timesta, `conversion_time` from  `data_stats_attributed_conversions_1` where DATE_FORMAT(`conversion_time`, '%Y-%m-%d %H:00:00') BETWEEN :strTime and :enTime group by `timesta`) p on t.`timeRange1` = p.`timesta` ;";
        LinkedHashMap<String,Double> conversionRateMap = handle.createQuery(actRateIntervalQuery).bind("strTime",strTime).bind("enTime",enTime).fold(new LinkedHashMap<String, Double>(), new Folder2<LinkedHashMap<String, Double>>() {
            @Override
            public LinkedHashMap<String, Double> fold(LinkedHashMap<String, Double> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
                accumulator.put(rs.getString("timeRange"), rs.getDouble("cnvrate"));
                return accumulator;
            }
        });

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(conversionRateMap).build();

    }

    @Produces("application/json")
    @GET
    @Path("/blendedcpxRate")
    @Timed
    public Response blendedCpxRate(@QueryParam("strTime") String strTime,@QueryParam("enTime") String enTime) throws ClassNotFoundException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        Date date = sdf.parse(strTime);
        long sTime = date.getTime();
        date = sdf.parse(enTime);
        long eTime = date.getTime();
        String blendedCpxQuery ="select from_unixtime(floor(`timeRange`/1000)) timeRange ,(sum(`cost`)/sum(`count`)) Cpx from `data_stats_engagements_realtime_v2`  where `timeRange` >= :strTime and `timeRange` <= :enTime GROUP BY `timeRange`;";
        LinkedHashMap<String,Double> blendedCpx = handle.createQuery(blendedCpxQuery).bind("strTime",sTime).bind("enTime",eTime).fold(new LinkedHashMap<String, Double>(), new Folder2<LinkedHashMap<String, Double>>() {
            @Override
            public LinkedHashMap<String, Double> fold(LinkedHashMap<String, Double> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
                accumulator.put(rs.getString("timeRange"), rs.getDouble("Cpx"));
                return accumulator;
            }
        });

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(blendedCpx).build();
    }

    @Produces("application/json")
    @GET
    @Path("/plareqrateinterval")
    @Timed
    public Response plaReqRateInterval(@QueryParam("strTime") String strTime,@QueryParam("enTime") String enTime) throws ClassNotFoundException, ParseException {

        String reqratequery ="select `interval_start`, sum(`requested_ads`) requested_ads from `data_report_summary_stats` where `interval_start` between :strTime and :enTime group by `interval_start`;";
        LinkedHashMap<String,Double> reqratedata = anyhandle.createQuery(reqratequery).bind("strTime",strTime).bind("enTime",enTime).fold(new LinkedHashMap<String, Double>(), new Folder2<LinkedHashMap<String, Double>>() {
            @Override
            public LinkedHashMap<String, Double> fold(LinkedHashMap<String, Double> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
                accumulator.put(rs.getString("interval_start"), rs.getDouble("requested_ads"));
                return accumulator;
            }
        });

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(reqratedata).build();
    }


    @Produces("application/json")
    @GET
    @Path("/plafillrateinterval")
    @Timed
    public Response plaFillRateInterval(@QueryParam("strTime") String strTime,@QueryParam("enTime") String enTime) throws ClassNotFoundException, ParseException {

        String fillratequery ="select `interval_start`, (sum(`impressions`)/sum(`requested_ads`))*100 fillrate from `data_report_summary_stats` where `interval_start` between :strTime and :enTime group by `interval_start`;";
        LinkedHashMap<String,Double> fillratedata = anyhandle.createQuery(fillratequery).bind("strTime",strTime).bind("enTime",enTime).fold(new LinkedHashMap<String, Double>(), new Folder2<LinkedHashMap<String, Double>>() {
            @Override
            public LinkedHashMap<String, Double> fold(LinkedHashMap<String, Double> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
                accumulator.put(rs.getString("interval_start"), rs.getDouble("fillrate"));
                return accumulator;
            }
        });

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(fillratedata).build();
    }

    public long getYesterday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        return c.getTimeInMillis();
    }

    private static long getToday(String date) throws ParseException {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date fmdate = sdf.parse(date);
            return fmdate.getTime();
    }

}