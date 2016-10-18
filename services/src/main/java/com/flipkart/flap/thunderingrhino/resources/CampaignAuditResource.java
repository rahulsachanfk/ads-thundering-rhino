package com.flipkart.flap.thunderingrhino.resources;


import com.flipkart.flap.thunderingrhino.entities.CampaignAudit;
import com.flipkart.flap.thunderingrhino.entities.CampaignDayReport;
import com.flipkart.flap.thunderingrhino.entities.CampaignDetails;
import com.flipkart.flap.thunderingrhino.entities.CampaignListingReport;
import com.flipkart.flap.thunderingrhino.utils.CampaignAuditDAO;
import com.flipkart.flap.thunderingrhino.utils.CampaignListReportDAO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.skife.jdbi.v2.Folder2;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.StatementContext;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by pavan.t on 22/06/15.
 */

@Path("/services/audit")
public class CampaignAuditResource {

    CampaignAuditDAO campaignAuditDAO;
    CampaignListReportDAO campaignListReportDAO;
    Handle handle;

    public CampaignAuditResource(CampaignAuditDAO campaignAuditDAO,CampaignListReportDAO campaignListReportDAO,Handle handle) {
        this.campaignAuditDAO = campaignAuditDAO;
        this.campaignListReportDAO = campaignListReportDAO;
        this.handle = handle;
    }

    @Path("/campaign/{campaignId}")
    @Produces("application/json")
    @GET
    public Response getcampaignAuditDetails(@PathParam("campaignId") String campaignId)  throws Exception {
        List<CampaignAudit> campaignAuditArrayList=new ArrayList<CampaignAudit>();

        try {

            campaignAuditArrayList = campaignAuditDAO.findCampaignAuditById(campaignId);

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(campaignAuditArrayList)
                .build();


    }

    @Path("/campaign/dayreport/{campaignId}")
    @Produces("application/json")
    @GET
    public Response CampaignDaywiseReport(@PathParam("campaignId") String campaignId,@Context HttpServletResponse response) throws Exception{
        List<CampaignDayReport> campaignDayReports=new ArrayList<>();

        final String fields[] = {"date", "View", "Action", "cost", "converted_units", "Revenue","indirect_converted_units", "indirect_revenue", "ROI"};

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("campaigns");
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            Cell fieldCell = headerRow.createCell(i);
            fieldCell.setCellValue(fields[i]);
        }

        try {
        campaignDayReports = campaignListReportDAO.campaignDayReport(campaignId);
        Iterator itr = campaignDayReports.iterator();
        int rownumber = 1;
        while (itr.hasNext()){
            CampaignDayReport cdr = (CampaignDayReport) itr.next();
            Row row = sheet.createRow(rownumber++);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(cdr.getDate());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(cdr.getViews());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(cdr.getAction());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(cdr.getCost());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(cdr.getConverted_units());

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(cdr.getRevenue());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(cdr.getIndirect_converted_units());

            Cell cell7 = row.createCell(7);
            cell7.setCellValue(cdr.getIndirect_revenue());

        }
        } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=wsrDaywisereport_" + campaignId + ".xls");
        wb.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        return Response.ok().build();
    }

    @Path("/campaign/listing/{campaignId}")
    @Produces("application/json")
    @GET
    public Response CampaignListingReport(@PathParam("campaignId") String campaignId, @Context HttpServletResponse response)  throws Exception {
        List<CampaignListingReport> listingReports=new ArrayList<>();
        List<CampaignListingReport> finalReports = new ArrayList<>();
        final String fields[] = {
                "CampaignId","BannerId","ListingId", "View", "Action", "Conversions", "Budget(Rs)", "Revenue(Rs)","AR", "CR", "ROI"};
        String query="select `ox_banner_id`,`listing_id` from listings where `ox_banner_id`>= :firstbanner and `ox_banner_id`<=:lastbanner";

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("campaigns");
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            Cell fieldCell = headerRow.createCell(i);
            fieldCell.setCellValue(fields[i]);
        }


        try {

            listingReports = campaignListReportDAO.campaignListingReport(campaignId);
            String firstbanner = listingReports.get(0).getBannerId();
            String lastbanner = listingReports.get(listingReports.size() - 1).getBannerId();

            LinkedHashMap<String,String > banerlist = handle.createQuery(query).bind("firstbanner",firstbanner).bind("lastbanner", lastbanner).fold(new LinkedHashMap<String, String>(), new Folder2<LinkedHashMap<String, String>>() {
                @Override
                public LinkedHashMap<String, String> fold(LinkedHashMap<String, String> accumulator, ResultSet rs, StatementContext ctx) throws SQLException {
                    accumulator.put(rs.getString("ox_banner_id"), rs.getString("listing_id"));
                    return accumulator;
                }
            });

            Iterator itr = listingReports.iterator();
            int rownumber = 1;
            while (itr.hasNext()){
                CampaignListingReport clr = (CampaignListingReport) itr.next();
                String bannerid = clr.getBannerId();
                String listid = banerlist.get(bannerid);
                clr.setListingid(listid);
                Row row = sheet.createRow(rownumber++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(clr.getCampaignId());

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(clr.getBannerId());

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(clr.getListingid());

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(clr.getView());

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(clr.getAction());

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(clr.getConversion());

                Cell cell6 = row.createCell(6);
                cell6.setCellValue(clr.getEx_budget());

                Cell cell7 = row.createCell(7);
                cell7.setCellValue(clr.getRevenue());

                Cell cell8 = row.createCell(8);
                cell8.setCellValue(clr.getAr());

                Cell cell9 = row.createCell(9);
                cell9.setCellValue(clr.getCvr());

                Cell cell10 = row.createCell(10);
                cell10.setCellValue(clr.getRoi());

            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + campaignId +".xls");
        wb.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        return Response.ok().build();

    }

    @Path("/campaign/listing")
    @Produces("application/json")
    @GET
    public Response CampaignListingMonthlyReport(@QueryParam("str_date") String str_date, @QueryParam("end_date") String end_date, @Context HttpServletResponse response)  throws Exception {
        List<CampaignListingReport> listingReports=new ArrayList<>();
        ArrayList<CampaignDetails> cmplist = new ArrayList<>();
        LinkedHashMap<String,CampaignDetails> linkedHashMap = new LinkedHashMap<>();
        final String campiagnFields[] = {
                "CampaignId", "CampaignName", "Status", "StartDate", "EndDate", "View", "Action", "Conversions","Ex_budget","Revenue", "AR", "CVR", "ROI"
        };

        cmplist = (ArrayList<CampaignDetails>) campaignAuditDAO.campaignDetailsList();
        listingReports = campaignListReportDAO.campaignListingMonthlyReport(str_date, end_date);
        Iterator itr = cmplist.iterator();
        while (itr.hasNext()){
            CampaignDetails campaignDetails = (CampaignDetails) itr.next();
            linkedHashMap.put(campaignDetails.getCampaignId(),campaignDetails);
        }

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("campaigns");
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < campiagnFields.length; i++) {
            Cell fieldCell = headerRow.createCell(i);
            fieldCell.setCellValue(campiagnFields[i]);
        }

        Iterator listitr = listingReports.iterator();
        int rownumber = 1;
        while (listitr.hasNext()){
            CampaignListingReport cmpobj = (CampaignListingReport) listitr.next();
            String cmpid = cmpobj.getCampaignId();
            CampaignDetails cmpDetails= linkedHashMap.get(cmpid);

            Row row = sheet.createRow(rownumber++);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(cmpDetails.getCampaignId());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(cmpDetails.getCampaignName());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(cmpDetails.getStatus());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(cmpDetails.getStartDate());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(cmpDetails.getEndDate());

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(cmpobj.getView());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(cmpobj.getAction());

            Cell cell7 = row.createCell(7);
            cell7.setCellValue(cmpobj.getConversion());

            Cell cell8 = row.createCell(8);
            cell8.setCellValue(cmpobj.getEx_budget());

            Cell cell9 = row.createCell(9);
            cell9.setCellValue(cmpobj.getRevenue());

            Cell cell10 = row.createCell(10);
            cell10.setCellValue(cmpobj.getAr());

            Cell cell11 = row.createCell(11);
            cell11.setCellValue(cmpobj.getCvr());

            Cell cell12 = row.createCell(12);
            cell12.setCellValue(cmpobj.getRoi());
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=wsrMonthly_"+ str_date +"_"+end_date+".xls");
        wb.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        return Response.ok().build();

    }




}
