package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rahul.sachan on 01/03/16.
 */
public class ProductModules {
    @JsonProperty
    private int productId;
    @JsonProperty
    private int moduleId;
    @JsonProperty
    private String moduleName;

    public ProductModules(int productId, int moduleId, String moduleName) {
        this.productId = productId;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public static class ProductModulesMapper implements ResultSetMapper<ProductModules>
    {
        @Override
        public ProductModules map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new ProductModules(r.getInt("productid"),r.getInt("moduleid"), r.getString("modulename"));
        }
    }
}
