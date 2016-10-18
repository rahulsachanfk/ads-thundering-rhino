package com.flipkart.flap.thunderingrhino.representations;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by suchi.sethi on 14/10/15.
 */
@Data
public class AdGroupListingResponse {
    private String count;

    private Map<String, Object> listingids = new HashMap<String, Object>();

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.listingids.put(name, value);
    }
}
