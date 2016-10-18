package com.flipkart.flap.thunderingrhino.representations;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by suchi.sethi on 13/10/15.
 */

@Data
public class CampaignAdGroupResponse {


    private String count;
    private String campaign_id;
  //  private String sellerId;
   // private String status;

    private Map<String, Object> GropuIDs = new HashMap<String, Object>();

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.GropuIDs.put(name, value);
    }
}
