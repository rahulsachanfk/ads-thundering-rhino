package com.flipkart.flap.thunderingrhino.services;

import com.flipkart.flap.thunderingrhino.entities.*;
import com.flipkart.flap.thunderingrhino.utils.BucketReportDAO;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ashishkumar.m
 * Date: 06/11/15
 * Time: 11:37 AM.
 */

public class BucketReportService {
    private BucketReportDAO bucketReportDAO;
    private static Logger logger = LoggerFactory.getLogger(BucketReportService.class);
    private String COMMA = ",";

    public BucketReportService(BucketReportDAO bucketReportDAO){
        this.bucketReportDAO = bucketReportDAO;
    }

    public BucketStatsResponse getAllbucketInfo(HourRequest hourRequest) {

        BucketStatsResponse bucketStatsResponse = new BucketStatsResponse();
        BucketHourlyStatsService bucketHourlyStatsService = new BucketHourlyStatsService(bucketReportDAO);
        bucketStatsResponse.setBucketStatInfoList(bucketHourlyStatsService.getAllbucketInfo(hourRequest));
        bucketStatsResponse.setBucketStatInfoBiasList(bucketHourlyStatsService.getAllbucketInfoBias(hourRequest));
        return bucketStatsResponse;

    }

    public List<BucketDailyStats> getAllBucketDailyStats(DailyConversionRequest dailyConversionRequest){

        BucketDailyStatsService bucketDailyStatsService = new BucketDailyStatsService(bucketReportDAO);
        List<BucketDailyStats> bucketDailyStatsList = bucketDailyStatsService.getAllBucketDailyStats(dailyConversionRequest);
        return bucketDailyStatsList;
    }
}
