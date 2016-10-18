package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rahul.sachan on 24/05/16.
 */
public class CampaignListingReport {
    @JsonProperty
    private String campaignId;
    private String bannerId;
    private String listingid;
    private int view;
    private int action;
    private int conversion;
    private double ex_budget;
    private double revenue;
    private double ar;
    private double cvr;
    private double roi;

    public CampaignListingReport(String campaignId, String bannerId, int view, int action, int conversion, double ex_budget, double revenue, double ar, double cvr, double roi) {
        this.campaignId = campaignId;
        this.bannerId = bannerId;
        this.listingid = listingid;
        this.view = view;
        this.action = action;
        this.conversion = conversion;
        this.ex_budget = ex_budget;
        this.revenue = revenue;
        this.ar = ar;
        this.cvr = cvr;
        this.roi = roi;
    }

    public CampaignListingReport(String campaignId, int view, int action, int conversion, double ex_budget, double revenue, double ar, double cvr, double roi) {
        this.campaignId = campaignId;
        this.view = view;
        this.action = action;
        this.conversion = conversion;
        this.ex_budget = ex_budget;
        this.revenue = revenue;
        this.ar = ar;
        this.cvr = cvr;
        this.roi = roi;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getListingid() {
        return listingid;
    }

    public void setListingid(String listingid) {
        this.listingid = listingid;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getConversion() {
        return conversion;
    }

    public void setConversion(int conversion) {
        this.conversion = conversion;
    }

    public double getEx_budget() {
        return ex_budget;
    }

    public void setEx_budget(double ex_budget) {
        this.ex_budget = ex_budget;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getAr() {
        return ar;
    }

    public void setAr(double ar) {
        this.ar = ar;
    }

    public double getCvr() {
        return cvr;
    }

    public void setCvr(double cvr) {
        this.cvr = cvr;
    }

    public double getRoi() {
        return roi;
    }

    public void setRoi(double roi) {
        this.roi = roi;
    }

    public static class CampaignListingMapper implements ResultSetMapper<CampaignListingReport>{
        public CampaignListingReport map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new CampaignListingReport(r.getString("campaign_id"),r.getString("banner_id"),r.getInt("views")
            ,r.getInt("action"),r.getInt("conversions"),r.getDouble("Ex_budget"),r.getDouble("revenue"),r.getDouble("AR"),r.getDouble("CVR"),r.getDouble("ROI"));
        }

    }

    public static class CampaignListingMonthlyMapper implements ResultSetMapper<CampaignListingReport> {
        public CampaignListingReport map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new CampaignListingReport(r.getString("campaign_id"),r.getInt("views"),r.getInt("action"),r.getInt("conversion"),r.getDouble("Ex_budget"), r.getDouble("revenue"), r.getDouble("AR"),r.getDouble("CVR"),r.getDouble("ROI"));
        }
    }

}
