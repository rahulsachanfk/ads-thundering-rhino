package com.flipkart.flap.thunderingrhino.representations;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by suchi.sethi on 13/10/15.
 */
@Data
public class CampaignImpClickResponse {

    private Map<String, Object> links;

    private Map<String, Object> clickImpressions = new HashMap<String, Object>();

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.clickImpressions.put(name, value);
    }
}
