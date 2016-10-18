package com.flipkart.flap.thunderingrhino.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pavan.t on 01/06/15.
 */


public class CampaignReport {
    private String SellerId;
    private String SellerName;
    private String CampaignId;
    private String CampaignName;
    private String Status;
    private Integer openXCampaignId;
    private Budget budget;
    private String startDate;
    private String endDate;
    private String RateId;
    private Float UsedBudget;
    private Integer Impressions;
    private Integer Clicks;
    private Integer Conversions;
    private Float Revenue;
    private Float CTR;
    private Float CVR;
    private Float ROI;
    private Float eCPA;

    public CampaignReport(String sellerId, String sellerName, String campaignId, String campaignName, String status, Integer openXCampaignId, Budget budget, String startDate, String endDate, String rateId, Float usedBudget, Integer impressions, Integer clicks, Integer conversions, Float revenue, Float CTR, Float CVR, Float ROI, Float eCPA) {
        SellerId = sellerId;
        SellerName = sellerName;
        CampaignId = campaignId;
        CampaignName = campaignName;
        Status = status;
        this.openXCampaignId = openXCampaignId;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        RateId = rateId;
        UsedBudget = usedBudget;
        Impressions = impressions;
        Clicks = clicks;
        Conversions = conversions;
        Revenue = revenue;
        this.CTR = CTR;
        this.CVR = CVR;
        this.ROI = ROI;
        this.eCPA = eCPA;
    }

    public String getSellerName() {
        return SellerName;
    }

    public String getSellerId() {
        return SellerId;
    }

    public String getCampaignId() {
        return CampaignId;
    }

    public String getCampaignName() {
        return CampaignName;
    }

    public String getStatus() {
        return Status;
    }

    public Integer getOpenXCampaignId() {
        return openXCampaignId;
    }

    public Budget getBudget() {
        return budget;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getRateId() {
        return RateId;
    }

    public Float getUsedBudget() {
        return UsedBudget;
    }

    public Integer getImpressions() {
        return Impressions;
    }

    public Integer getClicks() {
        return Clicks;
    }

    public Integer getConversions() {
        return Conversions;
    }

    public Float getRevenue() {
        return Revenue;
    }

    public Float getCTR() {
        return CTR;
    }

    public Float getCVR() {
        return CVR;
    }

    public Float getROI() {
        return ROI;
    }

    public Float geteCPA() {
        return eCPA;
    }


    public static class CampaignReportMapper implements ResultSetMapper<CampaignReport> {
        @Override
        public CampaignReport map(int index, ResultSet r, StatementContext ctx) throws SQLException {

            Budget budget = r.getBigDecimal("budget_value") == null ? new Budget(null, new BigDecimal(0)) :
                    new Budget(r.getString("budget_currency"), r.getBigDecimal("budget_value"));


            String StartDate=r.getTimestamp("start_date")==null ? "": r.getTimestamp("start_date").toString();

            String EndDate= r.getTimestamp("end_date")==null ? "": r.getTimestamp("end_date").toString();


            return new CampaignReport(r.getString("seller_id"),r.getString("seller_name"),r.getString("campaign_id"),r.getString("name"),
                    r.getString("status"),r.getInt("ox_campaign_id"),
                    budget,StartDate,EndDate,
                    r.getString("rate_id"),r.getFloat("usedbudget"),
                    r.getInt("impressions"),r.getInt("clicks"),r.getInt("conversions"),
                    r.getFloat("Revenue"),r.getFloat("CTR"),r.getFloat("CVR"),r.getFloat("ROI"),
                    r.getFloat("eCPA"));
        }
    }







}
