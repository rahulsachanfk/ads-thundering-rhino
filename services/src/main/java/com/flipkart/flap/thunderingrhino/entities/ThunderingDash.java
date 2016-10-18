package com.flipkart.flap.thunderingrhino.entities;


/**
 * Created by suchi.sethi on 20/08/15.
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ThunderingDash {
    @JsonProperty
    private String dash_key;
    @JsonProperty
    private String url;

    @JsonProperty
    @NotEmpty
    private String display;



    public String getdash_key() {
        return dash_key;
    }

    public void setdash_key(String url) {
        this.url = url;
    }

    public String geturl() {
        return url;
    }

    public void seturl(String url) {
        this.url = url;
    }

    public String getdisplay() {
        return display;
    }

    public void setdisplay(String display) {this.display = display;}


    public ThunderingDash(String key_value,  String url_value, String display_value) {
        dash_key = key_value;
        url=url_value;
        display= display_value;

    }

    public static class ThunderingDashMapper implements ResultSetMapper<ThunderingDash> {
        public ThunderingDash map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new ThunderingDash(
                    r.getString("dash_key"),
                    r.getString("url"),
                    r.getString("display"));


        }
    }


}


