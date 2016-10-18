package com.flipkart.flap.thunderingrhino.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by rahul.sachan on 29/02/16.
 */

public class CommonEntities {
    public static class getProducts implements ResultSetMapper<Map<Integer,String>> {
        @Override
        public Map<Integer, String> map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            Map<Integer,String> temp = new HashMap<Integer,String>();
            List<Map<Integer,String>> products = new ArrayList<>();
            temp.put(r.getInt("productid"),r.getString("productname"));
            products.add(temp);
            return temp;
        }

    }

}
