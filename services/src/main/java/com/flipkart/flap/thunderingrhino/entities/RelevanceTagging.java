package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by suchi.sethi on 11/01/16.
 */
public class RelevanceTagging {

    @JsonProperty
    private int id;

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public RelevanceTagging(int adverID) {
      int  id = adverID;

    }

    public static class RelevanceTaggingMapper implements ResultSetMapper<RelevanceTagging> {
        public RelevanceTagging map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new RelevanceTagging(r.getInt("id"));


        }
    }
}
