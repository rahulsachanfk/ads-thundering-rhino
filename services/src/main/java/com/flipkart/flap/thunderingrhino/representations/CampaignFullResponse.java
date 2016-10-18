package com.flipkart.flap.thunderingrhino.representations;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dipanjan.mukherjee on 17/02/15.
 *
 * Represents a campaign full response.
 */
@Data
public class CampaignFullResponse {
    private String campaignTitle;
    private String campaignId;
    private String sellerId;
    private String status;

    private Map<String, Object> goals;
  //  private ListingsResourceObject listings;
   // private Map<String,Object> progress;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
      this.additionalProperties.put(name, value);
    }
}
