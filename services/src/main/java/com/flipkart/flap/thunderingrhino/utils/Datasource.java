package com.flipkart.flap.thunderingrhino.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;

/**
 * Created by pavan.t on 21/05/15.
 */


public class Datasource {


    private static Datasource datasource;
    private ComboPooledDataSource cpds;

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://w3-ads-db-master.nm.flipkart.com:3306/adserverdb?autoReconnect=true";
    static final String USER = "adanalytics_ro";
    static final String PASS = "a08vYjXe";


    private Datasource() throws IOException, SQLException, PropertyVetoException {
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
        cpds.setJdbcUrl("jdbc:mysql://localhost:8888/adserverdb");
        cpds.setUser("adanalytics_ro");
        cpds.setPassword("a08vYjXe");


        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);
        cpds.setMaxConnectionAge(2*60*60);
        cpds.setMaxIdleTimeExcessConnections(30*60);

    }

    public static Datasource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new Datasource();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }

}