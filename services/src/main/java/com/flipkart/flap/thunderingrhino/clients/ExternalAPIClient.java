package com.flipkart.flap.thunderingrhino.clients;

import com.flipkart.flap.thunderingrhino.representations.*;
import com.sun.jersey.api.client.Client;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: Dipanjan Mukherjee (dipanjan.mukherjee@flipkart.com)
 *
 * A client to connect with advertising platform's external API. At
 * the moment, only contains methods to get campaign details.
 */
@RequiredArgsConstructor
public class ExternalAPIClient {
    @NonNull
    private Client client;

    @NonNull
    private String baseUrl;

    public CampaignFullResponse getCampaignDetails(String campaignId, String sellerid) {
        CampaignFullResponse campaign = this.client
                .resource(baseUrl)
                .path("/v2/pla/campaigns/")
                .path(campaignId)
              //  .path("performance")
                .header("X_SELLER_ID", sellerid)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(CampaignFullResponse.class);
System.out.println(campaign);
        return campaign;
    }


    public CampaignImpClickResponse getCampaignImpClick(String campaignId ,String sellerid) {
        CampaignImpClickResponse campaign = this.client
                .resource(baseUrl)
                .path("/v2/pla/campaigns/")
                .path(campaignId)
                .path("performance")
                .header("X_SELLER_ID", sellerid)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(CampaignImpClickResponse.class);
        System.out.println(campaign);
        return campaign;
    }

    public CampaignAdGroupResponse getCampaignAdGroups(String campaignId,String sellerid) {
        CampaignAdGroupResponse campaign = this.client
                .resource(baseUrl)
                .path("/v2/pla/campaigns/")
                .path(campaignId)
                .path("adgroups")
                .header("X_SELLER_ID", sellerid)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(CampaignAdGroupResponse.class);
        System.out.println(campaign);
        return campaign;
    }


    public CampaignAdGroupIdResponse getCampaignAdGroupsIDInfo(String campaignId, String adgroupid, String sellerid) {
        CampaignAdGroupIdResponse campaign = this.client
                .resource(baseUrl)
                .path("/v2/pla/campaigns/")
                .path(campaignId)
                .path("adgroups")
                .path(adgroupid)
                .header("X_SELLER_ID", sellerid)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(CampaignAdGroupIdResponse.class);
        System.out.println(campaign);
        return campaign;
    }

    public AdGroupListingResponse getAdGroupListingIdsInfo(String campaignId, String adgroupid,String sellerid) {
        AdGroupListingResponse campaign = this.client
                .resource(baseUrl)
                .path("/v2/pla/campaigns/")
                .path(campaignId)
                .path("adgroups")
                .path(adgroupid)
                .path("listings")
                .header("X_SELLER_ID", sellerid)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(AdGroupListingResponse.class);
        System.out.println(campaign);
        return campaign;
    }

}