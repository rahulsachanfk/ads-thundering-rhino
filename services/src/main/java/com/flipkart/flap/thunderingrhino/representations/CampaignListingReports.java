package com.flipkart.flap.thunderingrhino.representations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rahul.sachan on 01/06/16.
 */
public class CampaignListingReports{
    @JsonProperty
    private String campaignId;
    @JsonProperty
    private String campaignName;
    @JsonProperty
    private String startDate;
    @JsonProperty
    private String endDate;
    @JsonProperty
    private String status;
    @JsonProperty
    private int view;
    @JsonProperty
    private int action;
    @JsonProperty
    private int conversion;
    @JsonProperty
    private double ex_budget;
    @JsonProperty
    private double revenue;
    @JsonProperty
    private double ar;
    @JsonProperty
    private double cvr;
    @JsonProperty
    private double roi;
    public CampaignListingReports(String campaignId, String campaignName, String startDate, String endDate, String status, int view, int action, int conversion, double ex_budget, double revenue, double ar, double cvr, double roi) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.view = view;
        this.action = action;
        this.conversion = conversion;
        this.ex_budget = ex_budget;
        this.revenue = revenue;
        this.ar = ar;
        this.cvr = cvr;
        this.roi = roi;
    }
}
