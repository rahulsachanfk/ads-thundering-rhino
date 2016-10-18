package com.flipkart.flap.thunderingrhino.resources;

import com.sun.jersey.api.client.Client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by suchi.sethi on 19/05/16.
 */

@Path("/services/organicListings")
public class ListingIdResource {

    private final String FsnUrl;

    public ListingIdResource () {
        this.FsnUrl="sherlock/stores/";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson( @QueryParam("ip") String ip, @QueryParam("storePath") String storePath,  @QueryParam("tagId") String tagId, @QueryParam("facetId") String facetId) {

        String url = ip + FsnUrl + storePath + "/select?products.start=0&products.count=30000&context.internal=false&tags[]="  +  tagId + facetId;
        Client client = Client.create();
        String response = client.resource(url).get(String.class);
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET").entity(response).build();

    }

}

