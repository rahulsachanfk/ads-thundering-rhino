package com.flipkart.flap.thunderingrhino.services;

import com.flipkart.flap.thunderingrhino.entities.BucketStatInfo;
import com.flipkart.flap.thunderingrhino.entities.BucketStatInfoBias;
import com.flipkart.flap.thunderingrhino.entities.HourRequest;
import com.flipkart.flap.thunderingrhino.utils.BucketReportDAO;
import lombok.AllArgsConstructor;

import java.util.*;

public class BucketHourlyStatsService {
    private BucketReportDAO bucketReportDAO;
    private String COMMA = ",";

    public BucketHourlyStatsService(BucketReportDAO bucketReportDAO) {
        this.bucketReportDAO = bucketReportDAO;
    }

    public List<BucketStatInfo> getAllbucketInfo(HourRequest hourRequest) {
        List<BucketStatInfo> bucketStatInfos = new ArrayList<>();
        if (hourRequest.getBucketIds() == null || hourRequest.getBucketIds().isEmpty()) {
            if (hourRequest.getStartMonth() != hourRequest.getEndMonth()) {
                bucketStatInfos.addAll(bucketReportDAO.getAllBucketInfoAfter(hourRequest.getStartYear(), hourRequest.getStartMonth(), hourRequest.getStartDay()));
                bucketStatInfos.addAll(bucketReportDAO.getAllBucketInfoBefore(hourRequest.getEndYear(), hourRequest.getEndMonth(), hourRequest.getEndDay()));
            } else {
                bucketStatInfos = bucketReportDAO.getAllBucketInoByDateRange(hourRequest.getEndYear(), hourRequest.getEndMonth(), hourRequest.getEndDay(), hourRequest.getStartDay());

            }
            bucketStatInfos = getMarketBucketStats(bucketStatInfos);
        } else {
            if (hourRequest.getStartMonth() != hourRequest.getEndMonth()) {
                bucketStatInfos.addAll(bucketReportDAO.getBucketInfoAfter(hourRequest.getBucketIds(), hourRequest.getStartYear(), hourRequest.getStartMonth(), hourRequest.getStartDay()));
                bucketStatInfos.addAll(bucketReportDAO.getBucketInfoBefore(hourRequest.getBucketIds(), hourRequest.getEndYear(), hourRequest.getEndMonth(), hourRequest.getEndDay()));
            } else {
                bucketStatInfos.addAll(bucketReportDAO.getBucketInfoByDateRange(hourRequest.getBucketIds(), hourRequest.getEndYear(), hourRequest.getEndMonth(), hourRequest.getEndDay(), hourRequest.getStartDay()));
            }
        }

        if(!hourRequest.getContext().equalsIgnoreCase("all")) {
            bucketStatInfos = contextWiseBucketStats(bucketStatInfos, hourRequest.getContext());
        } else {
            bucketStatInfos = aggregateContextBucketStats(bucketStatInfos);
        }

        if(hourRequest.getAggregateLevel().equalsIgnoreCase("day") ){
            bucketStatInfos = getDailyAggregateResult(bucketStatInfos);
        }

        Collections.sort(bucketStatInfos);
        return bucketStatInfos;
    }

    public List<BucketStatInfoBias> getAllbucketInfoBias(HourRequest hourRequest) {
        List<BucketStatInfoBias> bucketStatInfoBiasList = new ArrayList<>();
        if (hourRequest.getBucketIds() == null || hourRequest.getBucketIds().isEmpty()) {
            if (hourRequest.getStartMonth() != hourRequest.getEndMonth()) {
                bucketStatInfoBiasList.addAll(bucketReportDAO.getAllBucketInfoAfterBias(hourRequest.getStartYear(), hourRequest.getStartMonth(), hourRequest.getStartDay()));
                bucketStatInfoBiasList.addAll(bucketReportDAO.getAllBucketInfoBeforeBias(hourRequest.getEndYear(), hourRequest.getEndMonth(), hourRequest.getEndDay()));
            } else {
                bucketStatInfoBiasList = bucketReportDAO.getAllBucketInoByDateRangeBias(hourRequest.getEndYear(), hourRequest.getEndMonth(), hourRequest.getEndDay(), hourRequest.getStartDay());

            }
        } else {
            if (hourRequest.getStartMonth() != hourRequest.getEndMonth()) {
                bucketStatInfoBiasList.addAll(bucketReportDAO.getBucketInfoAfterBias(hourRequest.getBucketIds(), hourRequest.getStartYear(), hourRequest.getStartMonth(), hourRequest.getStartDay()));
                bucketStatInfoBiasList.addAll(bucketReportDAO.getBucketInfoBeforeBias(hourRequest.getBucketIds(), hourRequest.getEndYear(), hourRequest.getEndMonth(), hourRequest.getEndDay()));
            } else {
                bucketStatInfoBiasList.addAll(bucketReportDAO.getBucketInfoByDateRangeBias(hourRequest.getBucketIds(), hourRequest.getEndYear(), hourRequest.getEndMonth(), hourRequest.getEndDay(), hourRequest.getStartDay()));
            }
        }
        if(hourRequest.getAggregateLevel().equalsIgnoreCase("day") ) {
            bucketStatInfoBiasList = getDailyAggregateResultBias(bucketStatInfoBiasList);
        }

        Collections.sort(bucketStatInfoBiasList);
        return bucketStatInfoBiasList;
    }

    private List<BucketStatInfoBias> getDailyAggregateResultBias(List<BucketStatInfoBias> bucketStatInfoBiasList) {
        Map<String, BucketStatInfoBias> aggregatedResults = new HashMap<>();
        bucketStatInfoBiasList.forEach(bucketStatInfoBias -> {
            String key = bucketStatInfoBias.getBucketId() + "_" + bucketStatInfoBias.getYear() + "_" + bucketStatInfoBias.getMonth() + "_" + bucketStatInfoBias.getDay();
            if(aggregatedResults.containsKey(key)){
                BucketStatInfoBias bucketStatInfoBias1 = aggregatedResults.get(key);
                bucketStatInfoBias1.setImpression(bucketStatInfoBias1.getImpression() + bucketStatInfoBias.getImpression());
                bucketStatInfoBias1.setViews(bucketStatInfoBias1.getViews() + bucketStatInfoBias.getViews());
                bucketStatInfoBias1.setEngagement(bucketStatInfoBias1.getEngagement() + bucketStatInfoBias.getEngagement());
                bucketStatInfoBias1.setRevenue(bucketStatInfoBias1.getRevenue() + bucketStatInfoBias.getRevenue());
                aggregatedResults.put(key, bucketStatInfoBias1);
            } else {
                bucketStatInfoBias.setStartHour(0);
                aggregatedResults.put(key, bucketStatInfoBias);
            }
        });

        aggregatedResults.entrySet().forEach(entry -> {
            Double actionRate = entry.getValue().getEngagement() / Double.valueOf(entry.getValue().getViews());
            Double ctr = entry.getValue().getEngagement() / Double.valueOf(entry.getValue().getImpression());
            Double cpc = entry.getValue().getRevenue() / Double.valueOf(entry.getValue().getEngagement());
            entry.getValue().setActionRate(actionRate);
            entry.getValue().setCtr(ctr);
            entry.getValue().setCpc(cpc);
        });
        return new ArrayList<>(aggregatedResults.values());
    }

    private List<BucketStatInfo> contextWiseBucketStats(List<BucketStatInfo> bucketStatInfos, String context) {

        List<BucketStatInfo> finalBucketStats = new ArrayList<>();
        bucketStatInfos.stream().forEach( bucketStatInfo -> {
            if(bucketStatInfo.getPageContext().equalsIgnoreCase(context)) {
                finalBucketStats.add(bucketStatInfo);
            }
        });
        return finalBucketStats;
    }

    private List<BucketStatInfo> aggregateContextBucketStats(List<BucketStatInfo> bucketStatInfos) {

        Map<String, BucketStatInfo> aggregatedResults = new HashMap<>();
        Map<String, Integer> keyFrequencyMap = new HashMap<>();
        final Map<String, BucketStatInfo> finalAggregatedResults = aggregatedResults;
        bucketStatInfos.stream().forEach(bucketStatInfo -> {
            String key = bucketStatInfo.getBucketId() + "_" + bucketStatInfo.getYear() + "_" + bucketStatInfo.getMonth() + "_" + bucketStatInfo.getDay() + "_" + bucketStatInfo.getStartHour();
            if (finalAggregatedResults.containsKey(key)) {
                finalAggregatedResults.put(key, addStats(finalAggregatedResults.get(key), bucketStatInfo));
                keyFrequencyMap.put(key, keyFrequencyMap.get(key) + 1);

            } else {
                bucketStatInfo.setPageContext("all");
                finalAggregatedResults.put(key, bucketStatInfo);
                keyFrequencyMap.put(key, 1);
            }
        });

        aggregatedResults = aggregatedMap(aggregatedResults, keyFrequencyMap);
        return new ArrayList<>(aggregatedResults.values());

    }

    private List<BucketStatInfo> getMarketBucketStats(List<BucketStatInfo> bucketStatsInfos) {

        Map<String, BucketStatInfo> finalBucketStats= new HashMap<>();
        Map<String, Integer> keyFrequencyMap = new HashMap<>();
        for(BucketStatInfo bucketStatInfo : bucketStatsInfos) {
            String key = bucketStatInfo.getPageContext() + "_" + bucketStatInfo.getYear() + "_" + bucketStatInfo.getMonth() + "_" + bucketStatInfo.getDay() + "_" + bucketStatInfo.getStartHour();
            if (finalBucketStats.containsKey(key)) {
                finalBucketStats.put(key, addStats(finalBucketStats.get(key), bucketStatInfo));
                keyFrequencyMap.put(key, keyFrequencyMap.get(key)+1);
            } else {
                keyFrequencyMap.put(key, 1);
                finalBucketStats.put(key, new BucketStatInfo("All", bucketStatInfo.getPageContext(), bucketStatInfo.getImpression(),bucketStatInfo.getViews(), bucketStatInfo.getEngagement(),
                        bucketStatInfo.getAvgPctr(), bucketStatInfo.getRankingScore(),bucketStatInfo.getActionRate(), bucketStatInfo.getCtr(), bucketStatInfo.getCpc(), bucketStatInfo.getRevenue(), "params", 100, bucketStatInfo.getYear(), bucketStatInfo.getMonth(), bucketStatInfo.getDay(), bucketStatInfo.getStartHour()));
            }
        }
        finalBucketStats = aggregatedMap(finalBucketStats, keyFrequencyMap);
        bucketStatsInfos.addAll(finalBucketStats.values());
        return bucketStatsInfos;
    }

    private List<BucketStatInfo> getDailyAggregateResult(List<BucketStatInfo> bucketStatsInfos) {
        Map<String, BucketStatInfo> aggregatedResults = new HashMap<>();
        Map<String, Integer> keyFrequencyMap = new HashMap<>();
        final Map<String, BucketStatInfo> finalAggregatedResults = aggregatedResults;
        bucketStatsInfos.stream().forEach(bucketStatInfo -> {
            String key = bucketStatInfo.getBucketId() + "_" + bucketStatInfo.getPageContext()+ "_" + bucketStatInfo.getYear() + "_" + bucketStatInfo.getMonth() + "_" + bucketStatInfo.getDay();
            if (finalAggregatedResults.containsKey(key)) {
                finalAggregatedResults.put(key, addStats(finalAggregatedResults.get(key), bucketStatInfo));
                keyFrequencyMap.put(key, keyFrequencyMap.get(key) + 1);
            } else {
                bucketStatInfo.setStartHour(0);
                finalAggregatedResults.put(key, bucketStatInfo);
                keyFrequencyMap.put(key, 1);
            }
        });
        aggregatedResults = aggregatedMap(aggregatedResults, keyFrequencyMap);
        return new ArrayList<>(aggregatedResults.values());
    }

    private BucketStatInfo addStats(BucketStatInfo bucketStatInfo, BucketStatInfo bucketLevelStat) {
        bucketStatInfo.setImpression(bucketLevelStat.getImpression() + bucketStatInfo.getImpression());
        bucketStatInfo.setViews(bucketLevelStat.getViews() + bucketStatInfo.getViews());
        bucketStatInfo.setEngagement(bucketLevelStat.getEngagement() + bucketStatInfo.getEngagement());
        bucketStatInfo.setAvgPctr(bucketLevelStat.getAvgPctr() + bucketStatInfo.getAvgPctr());
        bucketStatInfo.setRankingScore(bucketLevelStat.getRankingScore() + bucketStatInfo.getRankingScore());
        bucketStatInfo.setRevenue(bucketLevelStat.getRevenue() + bucketStatInfo.getRevenue());
        return bucketStatInfo;
    }

    private Map<String, BucketStatInfo> aggregatedMap(Map<String, BucketStatInfo> aggregatedResults, Map<String, Integer> keyFrequencyMap) {
        for(Map.Entry<String, BucketStatInfo> key : aggregatedResults.entrySet()) {
            Double actionRate = key.getValue().getEngagement() / Double.valueOf(key.getValue().getViews());
            Double ctr = key.getValue().getEngagement() / Double.valueOf(key.getValue().getImpression());
            Double avgPctr = key.getValue().getAvgPctr() / (keyFrequencyMap.get(key.getKey()));
            Double rankingScore = key.getValue().getRankingScore() / (keyFrequencyMap.get(key.getKey()));
            Double cpc = key.getValue().getRevenue() / key.getValue().getEngagement();
            key.getValue().setActionRate(actionRate);
            key.getValue().setCtr(ctr);
            key.getValue().setAvgPctr(avgPctr);
            key.getValue().setRankingScore(rankingScore);
            key.getValue().setCpc(cpc);
        }
        return aggregatedResults;
    }
}
