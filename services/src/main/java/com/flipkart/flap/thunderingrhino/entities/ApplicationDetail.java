package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suchi.sethi on 01/12/15.
 */
public class ApplicationDetail {
    @JsonProperty
    private String app_id;
    @JsonProperty
    private String instance_grp;

    @JsonProperty
    private String port;

    @JsonProperty
    private String url;

    @JsonProperty
    private String product;



    public String getapp_id() {
        return app_id;
    }

    public void setapp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getinstance_grp() {
        return instance_grp;
    }

    public void setinstance_grp(String instance_grp) {
        this.instance_grp = instance_grp;
    }

    public String getport() {
        return port;
    }

    public void setport(String port) {this.port = port;}

    public String geturl() {
        return url;
    }

    public void seturl(String url) {this.url = url;}

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public ApplicationDetail(String application_id,  String app_instance_grp, String health_port, String health_url) {
        app_id= application_id;
        instance_grp=app_instance_grp;
        port= health_port;
        url= health_url;

    }

    public ApplicationDetail(String app_id, String product){
        this.app_id= app_id;
        this.product= product;
    }

    public static class ApplicationDetailMapper implements ResultSetMapper<ApplicationDetail> {
        public ApplicationDetail map(int index, ResultSet r, StatementContext ctx) throws SQLException {

            System.out.println(new ApplicationDetail(
                    r.getString("app_id"),
                    r.getString("instance_grp"),
                    r.getString("port"),
                    r.getString("url")));

            return new ApplicationDetail(
                    r.getString("app_id"),
                    r.getString("instance_grp"),
                    r.getString("port"),
                    r.getString("url"));


        }
    }

    public static class appidListMapper implements ResultSetMapper<ApplicationDetail>{
        public ApplicationDetail map(int index, ResultSet r, StatementContext ctx) throws SQLException{
            ApplicationDetail applicationDetail = new ApplicationDetail(r.getString("product"),r.getString("app_id"));
            return applicationDetail;
        }
    }
}
