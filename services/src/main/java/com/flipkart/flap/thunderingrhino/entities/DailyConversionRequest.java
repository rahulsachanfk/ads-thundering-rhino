package com.flipkart.flap.thunderingrhino.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DailyConversionRequest {
    private List<String> bucketIds;
    private String pageContext;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int startYear;
    private int startMonth;
    private int startDay;
}
