package com.flipkart.flap.thunderingrhino.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by rahul.sachan on 01/06/16.
 */
public class CampaignDetails{
    private String campaignId;
    private String campaignName;
    private String startDate;
    private String endDate;
    private String status;

    public CampaignDetails(String campaignId, String campaignName, String startDate, String endDate, String status) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class CampaignDetailsMapper implements ResultSetMapper<CampaignDetails> {

        @Override
        public CampaignDetails map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new CampaignDetails(r.getString("campaign_id"),
                    r.getString("name"),r.getString("start_date"),
                    r.getString("end_date"),r.getString("status"));
        }
    }
}
