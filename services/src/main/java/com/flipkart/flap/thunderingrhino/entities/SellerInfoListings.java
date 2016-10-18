package com.flipkart.flap.thunderingrhino.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by suchi.sethi on 23/05/16.
 */
public class SellerInfoListings {
    private String ListingId;
    private String SellerId;
    private String SellerName;

    public String getListingId() {
        return ListingId;
    }

    public void setListingId(String listingId) {
        ListingId = listingId;
    }

    public String getSellerID() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public SellerInfoListings( String listingId, String sellerId, String sellerName) {

        ListingId = listingId;
        SellerId = sellerId;
        SellerName = sellerName;

    }

    public static class SellerInfoListingsMapper implements ResultSetMapper<SellerInfoListings> {

        public SellerInfoListings map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new SellerInfoListings(r.getString("listing_id"),
                    r.getString("seller_id"),
                    r.getString("seller_name"));


        }
    }
}
