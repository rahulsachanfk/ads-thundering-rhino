package com.flipkart.flap.thunderingrhino.resources;

import com.flipkart.flap.thunderingrhino.clients.ExternalAPIClient;
import com.sun.jersey.api.client.UniformInterfaceException;
import lombok.AllArgsConstructor;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.StringMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by dipanjan.mukherjee on 17/02/15.
 *
 * Resource to fetch a campaign's details from.
 */
@AllArgsConstructor
@Path("/services/campaigns")
public class CampaignDetailsResource {
    private ExternalAPIClient client;
    Handle handle;
    CampaignDetailsResource(Handle handle){
        this.handle = handle;
    }
    @Path("/{campaignId}")
    @Produces("application/json")
    @GET
    public Response getCampaignDetails(@PathParam("campaignId") String campaignId) {
        String sellerid = handle.createQuery("select `seller_id` from adserverdb.sellers left join adserverdb.campaigns on sellers.id=`advertiser_id` where `campaign_id`= :campaignId")
                .bind("campaignId", campaignId)
                .map(StringMapper.FIRST)
                .first();
        try {
            this.client.getCampaignDetails(campaignId,sellerid);
        } catch (UniformInterfaceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        System.out.println(Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getCampaignDetails(campaignId,sellerid))
                .build());
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getCampaignDetails(campaignId,sellerid))
                .build();
    }

    @Path("/{campaignId}/performance")
    @Produces("application/json")
    @GET


    public Response getCampaignImpClick(@PathParam("campaignId") String campaignId) {
        String sellerid = handle.createQuery("select `seller_id` from adserverdb.sellers left join adserverdb.campaigns on sellers.id=`advertiser_id` where `campaign_id`= :campaignId")
                .bind("campaignId", campaignId)
                .map(StringMapper.FIRST)
                .first();
        try {
            this.client.getCampaignImpClick(campaignId,sellerid);
        } catch (UniformInterfaceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        System.out.println(Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getCampaignImpClick(campaignId,sellerid))
                .build());

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getCampaignImpClick(campaignId,sellerid))
                .build();
    }


    @Path("/{campaignId}/adgroups")
    @Produces("application/json")
    @GET


    public Response getCampaignAdGroups(@PathParam("campaignId") String campaignId) {
        String sellerid = handle.createQuery("select `seller_id` from adserverdb.sellers left join adserverdb.campaigns on sellers.id=`advertiser_id` where `campaign_id`= :campaignId")
                .bind("campaignId", campaignId)
                .map(StringMapper.FIRST)
                .first();
        try {
            this.client.getCampaignAdGroups(campaignId,sellerid);
        } catch (UniformInterfaceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        System.out.println(Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getCampaignAdGroups(campaignId,sellerid))
                .build());

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getCampaignAdGroups(campaignId,sellerid))
                .build();
    }


    @Path("/{campaignId}/adgroups/{adgroupid}")
    @Produces("application/json")
    @GET


    public Response getCampaignAdGroups(@PathParam("campaignId") String campaignId, @PathParam("adgroupid") String adgroupid) {
        String sellerid = handle.createQuery("select `seller_id` from adserverdb.sellers left join adserverdb.campaigns on sellers.id=`advertiser_id` where `campaign_id`= :campaignId")
                .bind("campaignId", campaignId)
                .map(StringMapper.FIRST)
                .first();
        try {
            this.client.getCampaignAdGroupsIDInfo(campaignId, adgroupid,sellerid);
        } catch (UniformInterfaceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        System.out.println(Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getCampaignAdGroupsIDInfo(campaignId, adgroupid,sellerid))
                .build());

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getCampaignAdGroupsIDInfo(campaignId, adgroupid,sellerid))
                .build();
    }

    @Path("/{campaignId}/adgroups/{adgroupid}/listings")
    @Produces("application/json")
    @GET


    public Response getAdGroupListingIds(@PathParam("campaignId") String campaignId, @PathParam("adgroupid") String adgroupid) {
        String sellerid = handle.createQuery("select `seller_id` from adserverdb.sellers left join adserverdb.campaigns on sellers.id=`advertiser_id` where `campaign_id`= :campaignId")
                .bind("campaignId", campaignId)
                .map(StringMapper.FIRST)
                .first();
        try {
            this.client.getAdGroupListingIdsInfo(campaignId, adgroupid,sellerid);
        } catch (UniformInterfaceException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        System.out.println(Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getAdGroupListingIdsInfo(campaignId, adgroupid,sellerid))
                .build());

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(this.client.getAdGroupListingIdsInfo(campaignId, adgroupid,sellerid))
                .build();
    }

}


