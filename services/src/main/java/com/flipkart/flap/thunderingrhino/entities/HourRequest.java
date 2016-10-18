package com.flipkart.flap.thunderingrhino.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HourRequest {
    private List<String> bucketIds;
    private String aggregateLevel;
    private String context;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int startYear;
    private int startMonth;
    private int startDay;
}
