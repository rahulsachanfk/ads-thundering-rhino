package com.flipkart.flap.thunderingrhino.services;

import com.flipkart.flap.thunderingrhino.entities.BucketDailyStats;
import com.flipkart.flap.thunderingrhino.entities.BucketStatInfo;
import com.flipkart.flap.thunderingrhino.entities.DailyConversionRequest;
import com.flipkart.flap.thunderingrhino.utils.BucketReportDAO;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class BucketDailyStatsService {
    private BucketReportDAO bucketReportDAO;

    public List<BucketDailyStats> getAllBucketDailyStats(DailyConversionRequest dailyConversionRequest){
        List<BucketDailyStats> bucketDailyStatsList = new ArrayList<>();
        if(dailyConversionRequest.getBucketIds() == null || dailyConversionRequest.getBucketIds().isEmpty()){
            if(dailyConversionRequest.getStartMonth() != dailyConversionRequest.getEndMonth()) {
                bucketDailyStatsList.addAll(bucketReportDAO.getAllBucketDailyStatsTillEOM(dailyConversionRequest.getStartYear(), dailyConversionRequest.getStartMonth(), dailyConversionRequest.getStartDay()));
                bucketDailyStatsList.addAll(bucketReportDAO.getAllBucketDailyStatsTillEndDay(dailyConversionRequest.getEndYear(), dailyConversionRequest.getEndMonth(), dailyConversionRequest.getEndDay()));
            } else {
                bucketDailyStatsList.addAll(bucketReportDAO.getAllBucketDailyStats(dailyConversionRequest.getEndYear(), dailyConversionRequest.getEndMonth(), dailyConversionRequest.getEndDay(), dailyConversionRequest.getStartDay()));
            }
            bucketDailyStatsList = getMarketDailyBucketStats(bucketDailyStatsList);
        }else {
            if(dailyConversionRequest.getStartMonth() != dailyConversionRequest.getEndMonth()) {
                bucketDailyStatsList.addAll(bucketReportDAO.getBucketDailyStatsTillEOM(dailyConversionRequest.getBucketIds(), dailyConversionRequest.getStartYear(), dailyConversionRequest.getStartMonth(), dailyConversionRequest.getStartDay()));
                bucketDailyStatsList.addAll(bucketReportDAO.getBucketDailyStatsTillEndDay(dailyConversionRequest.getBucketIds(), dailyConversionRequest.getEndYear(), dailyConversionRequest.getEndMonth(), dailyConversionRequest.getEndDay()));
            } else {
                bucketDailyStatsList.addAll(bucketReportDAO.getBucketDailyStats(dailyConversionRequest.getBucketIds(), dailyConversionRequest.getEndYear(), dailyConversionRequest.getEndMonth(), dailyConversionRequest.getEndDay(), dailyConversionRequest.getStartDay()));
            }
        }

        if(!dailyConversionRequest.getPageContext().equalsIgnoreCase("all")) {
            bucketDailyStatsList = contextWiseBucketStats(bucketDailyStatsList, dailyConversionRequest.getPageContext());
        } else {
            bucketDailyStatsList = aggregateContextBucketStats(bucketDailyStatsList);
        }

        Collections.sort(bucketDailyStatsList);
        return bucketDailyStatsList;
    }

    private List<BucketDailyStats> getMarketDailyBucketStats(List<BucketDailyStats> bucketDailyStatsList) {
        Map<String, BucketDailyStats> finalBucketDailyStats= new HashMap<>();
        Map<String, Integer> keyFrequencyMap = new HashMap<>();
        for(BucketDailyStats bucketDailyStat : bucketDailyStatsList) {
            String key = bucketDailyStat.getPageContext() + "_" + bucketDailyStat.getYear() + "_" + bucketDailyStat.getMonth() + "_" + bucketDailyStat.getDay();
            if (finalBucketDailyStats.containsKey(key)) {
                BucketDailyStats bucketDailyStats = finalBucketDailyStats.get(key);
                bucketDailyStats.setConversion(bucketDailyStat.getConversion() + bucketDailyStats.getConversion());
                bucketDailyStats.setEngagements(bucketDailyStat.getEngagements() + bucketDailyStats.getEngagements());
                bucketDailyStats.setImpressions(bucketDailyStat.getImpressions() + bucketDailyStats.getImpressions());

                keyFrequencyMap.put(key, keyFrequencyMap.get(key)+1);
                finalBucketDailyStats.put(key, bucketDailyStats);
            } else {
                keyFrequencyMap.put(key, 1);
                finalBucketDailyStats.put(key, new BucketDailyStats("All", bucketDailyStat.getPageContext(), bucketDailyStat.getChannel(), bucketDailyStat.getCvr(), bucketDailyStat.getCvi(), bucketDailyStat.getConversion(),
                        bucketDailyStat.getEngagements(), bucketDailyStat.getImpressions(),bucketDailyStat.getAuc(), bucketDailyStat.getYear(), bucketDailyStat.getMonth(), bucketDailyStat.getDay()));
            }
        }

        finalBucketDailyStats.entrySet().stream().forEach(key ->{
            Double cvi = key.getValue().getConversion() / Double.valueOf(key.getValue().getImpressions());
            Double cvr = key.getValue().getConversion() / Double.valueOf(key.getValue().getEngagements());
            key.getValue().setCvi(cvi);
            key.getValue().setCvr(cvr);
        });
        bucketDailyStatsList.addAll(finalBucketDailyStats.values());
        return bucketDailyStatsList;
    }

    private List<BucketDailyStats> aggregateContextBucketStats(List<BucketDailyStats> bucketStatInfos) {

        Map<String, BucketDailyStats> aggregatedResults = new HashMap<>();
        final Map<String, BucketDailyStats> finalAggregatedResults = aggregatedResults;
        bucketStatInfos.stream().forEach(bucketStatInfo -> {
            String key = bucketStatInfo.getBucketId() + "_" + bucketStatInfo.getYear() + "_" + bucketStatInfo.getMonth() + "_" + bucketStatInfo.getDay();
            if (finalAggregatedResults.containsKey(key)) {
                finalAggregatedResults.put(key, addStats(finalAggregatedResults.get(key), bucketStatInfo));
            } else {
                bucketStatInfo.setPageContext("all");
                finalAggregatedResults.put(key, bucketStatInfo);
            }
        });

        aggregatedResults = aggregatedMap(aggregatedResults);
        return new ArrayList<>(aggregatedResults.values());

    }

    private BucketDailyStats addStats(BucketDailyStats bucketStatInfo, BucketDailyStats bucketLevelStat) {
        bucketStatInfo.setConversion(bucketLevelStat.getConversion() + bucketStatInfo.getConversion());
        bucketStatInfo.setImpressions(bucketLevelStat.getImpressions() + bucketStatInfo.getImpressions());
        bucketStatInfo.setEngagements(bucketLevelStat.getEngagements() + bucketStatInfo.getEngagements());
        return bucketStatInfo;
    }

    private Map<String, BucketDailyStats> aggregatedMap(Map<String, BucketDailyStats> aggregatedResults) {
        for(Map.Entry<String, BucketDailyStats> key : aggregatedResults.entrySet()) {
            Double cvr = key.getValue().getConversion() / Double.valueOf(key.getValue().getEngagements());
            Double cvi = key.getValue().getConversion() / Double.valueOf(key.getValue().getImpressions());
            key.getValue().setCvr(cvr);
            key.getValue().setCvi(cvi);
        }
        return aggregatedResults;
    }

    private List<BucketDailyStats> contextWiseBucketStats(List<BucketDailyStats> bucketStatInfos, String context) {

        List<BucketDailyStats> finalBucketStats = new ArrayList<>();
        bucketStatInfos.stream().forEach( bucketStatInfo -> {
            if(bucketStatInfo.getPageContext().equalsIgnoreCase(context)) {
                finalBucketStats.add(bucketStatInfo);
            }
        });
        return finalBucketStats;
    }
}
