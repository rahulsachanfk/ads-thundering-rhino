package com.flipkart.flap.thunderingrhino.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by pavan.t on 01/06/15.
 */

public class SellerReport {



    public static final String DRAFT = "draft";
    public static final String READY = "ready";
    public static final String ABORTED = "aborted";
    public static final String PAUSED = "paused";
    public static final String RUNNING = "running";
    public static final String COMPLETE = "complete";

    private String SellerId;
    private String SellerName;
    private String CampaignId;
    private String CampaignName;
    private String Status;
    private Integer openXCampaignId;
    private Budget budget;
    private Date startDate;
    private Date endDate;
    private String RateId;
    private Float UsedBudget;
    private Integer BannerId;
    private String ListingId;
    private Integer Impressions;
    private Integer Clicks;
    private Integer Conversions;
    private Float Revenue;
    private Float CTR;
    private Float CVR;
    private Float ROI;
    private Float eCPA;


    public SellerReport(String SellerName,String sellerId, String campaignId, String campaignName, String status, Integer openXCampaignId, Budget budget, Date startDate, Date endDate, String rate_id, Float usedBudget, Integer BannerId,String listingId, Integer impressions, Integer clicks, Integer conversions, Float revenue, Float CTR, Float CVR, Float ROI, Float eCPA) {
        this.SellerName=SellerName;
        this.SellerId = sellerId;
        this.CampaignId = campaignId;
        this.CampaignName = campaignName;
        this.Status = status;
        this.openXCampaignId = openXCampaignId;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.RateId = rate_id;
        this.UsedBudget = usedBudget;
        this.BannerId=BannerId;
        this.ListingId = listingId;
        this.Impressions = impressions;
        this.Clicks = clicks;
        this.Conversions = conversions;
        this.Revenue = revenue;
        this.CTR = CTR;
        this.CVR = CVR;
        this.ROI = ROI;
        this.eCPA = eCPA;
    }


    public String getSellerId() {
        return SellerId;
    }

    public String getSellerName(){
        return SellerName;
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getRate_id() {
        return RateId;
    }

    public Float getUsedBudget() {
        return UsedBudget;
    }

    public Integer BannerId() {
        return BannerId;
    }

    public String getListingId() {
        return ListingId;
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


    public static class SellerReportMapper implements ResultSetMapper<SellerReport> {
        @Override
        public SellerReport map(int index, ResultSet r, StatementContext ctx) throws SQLException {

            Budget budget = r.getBigDecimal("budget_value") == null ? new Budget(null, new BigDecimal(0)) :
                    new Budget(r.getString("budget_currency"), r.getBigDecimal("budget_value"));

            return new SellerReport(r.getString("seller_name"),r.getString("seller_id"),r.getString("campaign_id"),r.getString("name"),
                    r.getString("status"),r.getInt("ox_campaign_id"),
                    budget,r.getTimestamp("start_date"),r.getTimestamp("end_date"),
                    r.getString("rate_id"),r.getFloat("usedbudget"),r.getInt("bannerid"),r.getString("listing_id"),
                    r.getInt("impressions"),r.getInt("clicks"),r.getInt("conversions"),
                    r.getFloat("Revenue"),r.getFloat("CTR"),r.getFloat("CVR"),r.getFloat("ROI"),
                    r.getFloat("eCPA"));
        }
    }



}
