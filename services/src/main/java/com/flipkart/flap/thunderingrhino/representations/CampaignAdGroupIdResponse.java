package com.flipkart.flap.thunderingrhino.representations;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by suchi.sethi on 14/10/15.
 */
@Data
public class CampaignAdGroupIdResponse {
    private String id;
    private String name;


    private Map<String, Object> budget;
    private String status;
    private Map<String, Object> listings;
    private String created_time;
    private String last_modified_time;


    //  private ListingsResourceObject listings;
    // private Map<String,Object> progress;

   // @JsonIgnore
    private Map<String, Object> additionalProp = new HashMap<String, Object>();

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProp.put(name, value);
    }
}
