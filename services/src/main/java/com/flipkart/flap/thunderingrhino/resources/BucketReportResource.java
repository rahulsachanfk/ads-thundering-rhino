package com.flipkart.flap.thunderingrhino.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.flipkart.flap.thunderingrhino.entities.*;
import com.flipkart.flap.thunderingrhino.services.BucketReportService;
import com.flipkart.flap.thunderingrhino.utils.DateTimeUtil;
import com.flipkart.flap.thunderingrhino.utils.JacksonObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ashishkumar.m
 * Date: 06/11/15
 * Time: 11:36 AM.
 */

@Path("/bucket/")
@Produces(MediaType.APPLICATION_JSON)
public class BucketReportResource {
    private BucketReportService bucketReportService;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final List<String> aggregateValues = new ArrayList<>(Arrays.asList("hour", "day"));
    private final List<String> contextValues = new ArrayList<>(Arrays.asList("search", "browse", "all"));
    private List<String> bucket_Ids = new ArrayList<>();

    public BucketReportResource(BucketReportService bucketReportService){
        this.bucketReportService = bucketReportService;
    }
    @GET
    @Path("/stats")
    @Timed
    public Object getBucketStats(@QueryParam("bucketId") String bucketIds, @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate,@DefaultValue("hour") @QueryParam("aggregate") String aggregateLevel,@DefaultValue("all") @QueryParam("context") String context, @QueryParam("callback") String callBackFunction) {

        Object response = null;
        try {
            if (endDate == null || endDate.isEmpty()) {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                        .entity("End Date is mandatory parameter.")
                        .build());
            }

            Date end = formatter.parse(endDate);
            Date start = startDate == null || startDate.isEmpty() ? end : formatter.parse(startDate);
            Boolean notBadRequest = verifyParams(start, end, aggregateLevel, context);

            if(notBadRequest) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start);
                Integer startYear = calendar.get(Calendar.YEAR);
                Integer startMonth = calendar.get(Calendar.MONTH) + 1;
                Integer startDay = calendar.get(Calendar.DAY_OF_MONTH);

                calendar.setTime(end);
                Integer endYear = calendar.get(Calendar.YEAR);
                Integer endMonth = calendar.get(Calendar.MONTH) + 1;
                Integer endDay = calendar.get(Calendar.DAY_OF_MONTH);

                if(bucketIds != null) {
                    bucket_Ids = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(bucketIds));
                } else{
                    bucket_Ids = new ArrayList<>();
                }
                HourRequest hourRequest = new HourRequest(bucket_Ids, aggregateLevel, context, endYear, endMonth, endDay, startYear, startMonth, startDay);
                BucketStatsResponse bucketStatsResponse = bucketReportService.getAllbucketInfo(hourRequest);

                DailyConversionRequest dailyConversionRequest = new DailyConversionRequest(bucket_Ids, context,endYear, endMonth, endDay, startYear, startMonth, startDay);
                List<BucketDailyStats> bucketDailyStatses = bucketReportService.getAllBucketDailyStats(dailyConversionRequest);

                bucketStatsResponse.setBucketDailyStatsList(bucketDailyStatses);

                response = callBackFunction == null ? bucketStatsResponse : convertToJsonP(bucketStatsResponse, callBackFunction);
            }
        } catch (ParseException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build());
        }
        return response;
    }

    @GET
    @Path("/stats/daily")
    @Timed
    public Object getDailyBucketStats(@QueryParam("bucketId") String bucketIds,@DefaultValue("all") @QueryParam("bucketId") String pageContext, @QueryParam("startDate") String startDay, @QueryParam("endDate") String endDay, @QueryParam("callback") String callBackFunction){

        Object response = null;
        try {
            Date endDate = formatter.parse(endDay);
            Date startDate = startDay == null || startDay.isEmpty() ? endDate : formatter.parse(startDay);
            Boolean notBadRequest = verifyParams(startDate, endDate, "day", "all");

            if(notBadRequest){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                Integer startYear = calendar.get(Calendar.YEAR);
                Integer startMonth = calendar.get(Calendar.MONTH) + 1;
                Integer startDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                calendar.setTime(endDate);
                Integer endYear = calendar.get(Calendar.YEAR);
                Integer endMonth = calendar.get(Calendar.MONTH) + 1;
                Integer endDayofMonth = calendar.get(Calendar.DAY_OF_MONTH);


                if(bucketIds != null) {
                    bucket_Ids = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(bucketIds));
                } else{
                    bucket_Ids = new ArrayList<>();
                }
                DailyConversionRequest dailyConversionRequest = new DailyConversionRequest(bucket_Ids, pageContext, endYear, endMonth, endDayofMonth, startYear, startMonth, startDayOfMonth);

                List<BucketDailyStats> bucketDailyStatses = bucketReportService.getAllBucketDailyStats(dailyConversionRequest);

                if (bucketDailyStatses != null) {
                    Collections.sort(bucketDailyStatses);
                }
                response = callBackFunction == null ? bucketDailyStatses : convertToJsonP(bucketDailyStatses, callBackFunction);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Boolean verifyParams(Date startDate, Date endDate, String aggregateLevel, String context) {

        long days = (endDate.getTime() - startDate.getTime());
        days = TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);

        if (!DateTimeUtil.equalOrBefore(startDate, endDate)) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("End Date should be greater than Start Date.")
                    .build());
        }

        if (days > 30) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Start date and End date interval should not be greater than 30 days.")
                    .build());
        }


        if (!aggregateValues.contains(aggregateLevel)) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Aggregate Level should be hour or day.")
                    .build());
        }
        if (!contextValues.contains(context)) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Context Level should be search or browse.")
                    .build());
        }
        return true;
    }


    private String convertToJsonP(Object o,String jsonpCallback){
        String outputMessage = null;
        com.fasterxml.jackson.databind.ObjectMapper mapper = JacksonObjectMapper.INSTANCE.getObjectMapper();
        try {
            outputMessage=mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            outputMessage = "Not able to parse json.";
        }
        if(outputMessage != null){
            outputMessage=jsonpCallback + "(" + outputMessage + ")";
        }
        return outputMessage;
    }
}
