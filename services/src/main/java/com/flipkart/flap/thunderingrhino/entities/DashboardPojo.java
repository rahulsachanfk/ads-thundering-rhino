package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by rahul.sachan on 08/03/16.
 */
public class DashboardPojo {

    @JsonProperty
    private String productid;
    @JsonProperty
    private String moduleid;
    @JsonProperty
    private String newModuleName;
    @JsonProperty
    @NotEmpty
    @NotNull
    private String dash_key;
    @JsonProperty
    @NotEmpty
    @NotNull
    private String url;
    @JsonProperty
    @NotEmpty
    @NotNull
    private String display;

    public DashboardPojo(){}
    public DashboardPojo(String productid, String moduleid, String newModuleName, String dash_key, String url, String display) {
        this.productid = productid;
        this.moduleid = moduleid;
        this.newModuleName = newModuleName;
        this.dash_key = dash_key;
        this.url = url;
        this.display = display;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getModuleid() {
        return moduleid;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid;
    }

    public String getNewModuleName() {
        return newModuleName;
    }

    public void setNewModuleName(String newModuleName) {
        this.newModuleName = newModuleName;
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
}
