package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rahul.sachan on 17/02/16.
 */
public class NewDashboard {
    @JsonProperty
    private String productname;
    @JsonProperty
    private String modulename;
    @JsonProperty
    private String dash_key;
    @JsonProperty
    private String url;
    @JsonProperty
    @NotEmpty
    private String display;

    public NewDashboard(String productname, String modulename, String dash_key, String url, String display) {
        this.productname = productname;
        this.modulename = modulename;
        this.dash_key = dash_key;
        this.url = url;
        this.display = display;
    }

    public String getDash_key() {
        return dash_key;
    }

    public void setDash_key(String dash_key) {
        this.dash_key = dash_key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    public static class NewDashboardMapper implements ResultSetMapper<NewDashboard>
    {
        @Override
        public NewDashboard map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new NewDashboard(r.getString("productname"), r.getString("modulename"),r.getString("dash_key"),r.getString("url"),r.getString("display"));
        }
    }
}


