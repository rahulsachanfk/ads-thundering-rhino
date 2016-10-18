package com.flipkart.flap.thunderingrhino.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pavan.t on 19/06/15.
 */
public class SherlockListing {


    private String Campaign_Id;
    private String Listing_Id;
    private int Supressed;
    private String Product_Id;


    public SherlockListing() {
    }

    public SherlockListing(String campaign_Id,String listing_Id, int supressed, String product_Id) {
        this.Listing_Id = listing_Id;
        this.Campaign_Id = campaign_Id;
        this.Supressed = supressed;
        this.Product_Id = product_Id;
    }

    public String getCampaign_Id() {
        return this.Campaign_Id;
    }

    public void setCampaign_Id(String campaign_Id) {
        this.Campaign_Id = campaign_Id;
    }

    public String getListing_Id() {
        return this.Listing_Id;
    }

    public void setListing_Id(String listing_Id) {
        this.Listing_Id = listing_Id;
    }

    public int getSupressed() {
        return this.Supressed;
    }

    public void setSupressed(int supressed) {
        this.Supressed = supressed;
    }

    public String getProduct_Id() {
        return this.Product_Id;
    }

    public void setProduct_Id(String product_Id) {
        this.Product_Id = product_Id;
    }


    public static class SherlockListingMapper implements ResultSetMapper<SherlockListing> {
        @Override
        public SherlockListing map(int index, ResultSet r, StatementContext ctx) throws SQLException {

            return new SherlockListing(r.getString("campaign_id"),r.getString("listing_id"),r.getInt("suppressed"),
                    r.getString("product_id"));

            }
        }




}
