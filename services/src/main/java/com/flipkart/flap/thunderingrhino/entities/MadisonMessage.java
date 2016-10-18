package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pushya.gupta on 07/06/16.
 */
public class MadisonMessage {
    @JsonProperty
    private String message ;

    @JsonProperty
    private String custom_header ;

    public MadisonMessage(String message , String custom_header) {
        this.message = message;
        this.custom_header = custom_header;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setCustom_header(String custom_header) {
        this.custom_header = custom_header;
    }

    public String getCustom_header() {
        return custom_header;
    }


    public static class MadisonMessageMapper implements ResultSetMapper<MadisonMessage>{
        public MadisonMessage map(int index, ResultSet r, StatementContext ctx) throws SQLException{
            return new MadisonMessage(
                    r.getString("message"),
                    r.getString("custom_headers"));
        }
    }








}
