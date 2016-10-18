package com.flipkart.flap.thunderingrhino.representations;

import lombok.Data;

/**
 * Created by pavan.t on 19/06/15.
 */

@Data
public class SherlockProductResponse {
    private String Product_Id;
    private int Suppressed;
    private int MasterFound;
    private int slaveFound;

}
