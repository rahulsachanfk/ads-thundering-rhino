package com.flipkart.flap.thunderingrhino.entities;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rahul.sachan on 21/09/16.
 */
public class CampaignDayReport {
    private String date;
    private int views;
    private int action;
    private double cost;
    private int converted_units;
    private double revenue;
    private int indirect_converted_units;
    private double indirect_revenue;

    public CampaignDayReport(String date, int views, int action, double cost, int converted_units, double revenue, int indirect_converted_units, double indirect_revenue) {
        this.date = date;
        this.views = views;
        this.action = action;
        this.cost = cost;
        this.converted_units = converted_units;
        this.revenue = revenue;
        this.indirect_converted_units = indirect_converted_units;
        this.indirect_revenue = indirect_revenue;
    }

    public String getDate() {
        return date;
    }

    public int getViews() {
        return views;
    }

    public int getAction() {
        return action;
    }

    public double getCost() {
        return cost;
    }

    public int getConverted_units() {
        return converted_units;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getIndirect_converted_units() {
        return indirect_converted_units;
    }

    public double getIndirect_revenue() {
        return indirect_revenue;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setConverted_units(int converted_units) {
        this.converted_units = converted_units;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void setIndirect_converted_units(int indirect_converted_units) {
        this.indirect_converted_units = indirect_converted_units;
    }

    public void setIndirect_revenue(double indirect_revenue) {
        this.indirect_revenue = indirect_revenue;
    }

    @Override
    public String toString() {
        return "CampaignDayReport{" +
                "date='" + date + '\'' +
                ", views=" + views +
                ", action=" + action +
                ", cost=" + cost +
                ", converted_units=" + converted_units +
                ", revenue=" + revenue +
                ", indirect_converted_units=" + indirect_converted_units +
                ", indirect_revenue=" + indirect_revenue +
                '}';
    }

    public static class CampaignDayReportMapper implements ResultSetMapper<CampaignDayReport> {
        @Override
        public CampaignDayReport map(int index, ResultSet r, StatementContext ctx) throws SQLException {

            return new CampaignDayReport(r.getString("date"),r.getInt("views"),r.getInt("action"),r.getDouble("cost"),r.getInt("converted_units"),
                    r.getDouble("revenue"),r.getInt("indirectly_converted_units"),r.getDouble("indirect_revenue"));

        }
    }
}
