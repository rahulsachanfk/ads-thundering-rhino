package com.flipkart.flap.thunderingrhino.representations;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Yoihenba Kongbam (yoihenba.k@flipkart.com)
 * Date: 18/02/15
 */
@Data
public class CampaignProgressObject {

    private int impressions;
    private int clicks;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
