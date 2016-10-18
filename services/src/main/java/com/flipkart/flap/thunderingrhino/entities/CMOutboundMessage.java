package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pushya.gupta on 30/05/16.
 */
public class CMOutboundMessage {
    @JsonProperty
    private String message ;


    public CMOutboundMessage(String message) {
        this.message = message;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }




    public static class CMOutboundMessageMapper implements ResultSetMapper<CMOutboundMessage> {
        public CMOutboundMessage map(int index, ResultSet r, StatementContext ctx) throws SQLException
        {
            return new CMOutboundMessage(r.getString("message"));
        }
    }

}
