package com.flipkart.flap.thunderingrhino.entities;

import lombok.Data;
import lombok.Setter;
import java.util.List;


@Data
public class BucketStatsResponse {

    List<BucketStatInfo> bucketStatInfoList;
    List<BucketStatInfoBias> bucketStatInfoBiasList;
    List<BucketDailyStats> bucketDailyStatsList;
}
