package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by suchi.sethi on 12/01/16.
 */
public class ProductDetail {
    @JsonProperty
    private String product_id;

    public ProductDetail(String productId) {
        this.product_id = productId;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public static class ProductDetailMapper implements ResultSetMapper<ProductDetail> {
        public ProductDetail map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new ProductDetail(
                    r.getString("product_id"));



        }
    }
}
