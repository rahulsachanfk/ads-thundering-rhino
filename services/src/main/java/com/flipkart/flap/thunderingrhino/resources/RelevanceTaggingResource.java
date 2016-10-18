package com.flipkart.flap.thunderingrhino.resources;

import com.flipkart.flap.thunderingrhino.entities.CampaignInfo;
import com.flipkart.flap.thunderingrhino.entities.ProductDetail;
import com.flipkart.flap.thunderingrhino.utils.RelevanceTaggingDAO;
import com.sun.jersey.api.client.Client;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suchi.sethi on 11/01/16.
 */

@Path("/services/seller")


public class RelevanceTaggingResource {

    RelevanceTaggingDAO relevanceTaggingDAO;
    public RelevanceTaggingResource(RelevanceTaggingDAO relevanceTaggingDAO) {
        this.relevanceTaggingDAO = relevanceTaggingDAO;
    }



    @Path("/{sellerID}")
    @Produces("application/json")
    @GET
    public Response getAdid(@PathParam("sellerID") String sellerID) {

        int Adid = 0;
        try {
            Adid = relevanceTaggingDAO.getAdIDbySellerID(sellerID);
        } catch (Exception e) {
            e.printStackTrace();

        }

        System.out.println(Response.ok(Adid)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .build());

        return javax.ws.rs.core.Response.ok(Adid)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .build();


    }

    @Path("/campaign/{adID}")
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Response getCampaignInfo(@PathParam("adID") String adID) {

        List<CampaignInfo> campaignInfoList = new ArrayList<>();

        try {
            campaignInfoList =  relevanceTaggingDAO.getCampaignbyAdID(adID);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(campaignInfoList)
                .build();


    }

    @Path("/listings/{campId}")
    @GET
    @Produces("application/json")
    @Consumes("application/json")

    public Response getProductID(@PathParam("campId") String campId) {

        List<ProductDetail> productIDList = new ArrayList<>();

        try {
            productIDList =  relevanceTaggingDAO.getProductIDbyCampaign(campId);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(productIDList)
                .build();


    }

    @Path("/tagging")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIgorJson(@QueryParam("prodID") String prodID) {

        String url = "http://10.65.30.95:26600/sp-cms-backend/rest/entity/product/" + prodID ;

        Client client = Client.create();
        String response = client.resource(url).get(String.class);
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET").entity(response).build();

    }

}



