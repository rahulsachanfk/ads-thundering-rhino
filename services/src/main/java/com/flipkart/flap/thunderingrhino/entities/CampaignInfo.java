package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by suchi.sethi on 11/01/16.
 */
public class CampaignInfo {
    @JsonProperty
    private String campaign_id;
    @JsonProperty
    private String name;

    @JsonProperty
    private String status;


    public CampaignInfo(String campaignId, String campaignName, String campaignStatus) {

        this.campaign_id = campaignId;
        this.name = campaignName;
        this.status = campaignStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class CampaignInfoMapper implements ResultSetMapper<CampaignInfo> {
        public CampaignInfo map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new CampaignInfo(
                    r.getString("campaign_id"),
                    r.getString("name"),
                    r.getString("status"));


        }
    }



}

