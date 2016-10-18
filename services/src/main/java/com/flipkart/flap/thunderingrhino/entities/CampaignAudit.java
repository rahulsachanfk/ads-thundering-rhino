package com.flipkart.flap.thunderingrhino.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pavan.t on 22/06/15.
 */
public class CampaignAudit{

        private int Id;
        private String CampaignId;
        private String Source_Status;
        private String Target_Status;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCampaignId() {
        return CampaignId;
    }

    public void setCampaignId(String campaignId) {
        CampaignId = campaignId;
    }

    public String getSource_Status() {
        return Source_Status;
    }

    public void setSource_Status(String source_Status) {
        Source_Status = source_Status;
    }

    public String getTarget_Status() {
        return Target_Status;
    }

    public void setTarget_Status(String target_Status) {
        Target_Status = target_Status;
    }

    public String getAction_performed() {
        return Action_performed;
    }

    public void setAction_performed(String action_performed) {
        Action_performed = action_performed;
    }

    public String getTimestamp_action() {
        return Timestamp_action;
    }

    public void setTimestamp_action(String timestamp_action) {
        Timestamp_action = timestamp_action;
    }

    public String getCampaignEdits() {
        return campaignEdits;
    }

    public void setCampaignEdits(String campaignEdits) {
        this.campaignEdits = campaignEdits;
    }

    public int getProccessed() {
        return Proccessed;
    }

    public void setProccessed(int proccessed) {
        Proccessed = proccessed;
    }

    private String Action_performed;
        private String Timestamp_action;
        private String campaignEdits;
        private int Proccessed;

    public CampaignAudit(int id, String campaignId, String source_Status, String target_Status, String action_performed, String timestamp_action, String campaignEdits, int proccessed) {
        Id = id;
        CampaignId = campaignId;
        Source_Status = source_Status;
        Target_Status = target_Status;
        Action_performed = action_performed;
        Timestamp_action = timestamp_action;
        this.campaignEdits = campaignEdits;
        Proccessed = proccessed;
    }



    public CampaignAudit() {
    }




    public static class CampaignAuditMapper implements ResultSetMapper<CampaignAudit> {
        @Override
        public CampaignAudit map(int index, ResultSet r, StatementContext ctx) throws SQLException {




            return new CampaignAudit(r.getInt("Id"),
                    r.getString("campaign_id"),
                    r.getString("source_status"),
                    r.getString("target_status"),
                    r.getString("action_performed"),
                    r.getString("timestamp_action"),
                    r.getString("campaignEdits"),
                    r.getInt("processed"));

        }
    }


}
