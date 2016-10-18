package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by pavan.t on 26/05/16.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @JsonProperty
    List<String> productIds;
}
