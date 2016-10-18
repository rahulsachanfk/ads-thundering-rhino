package com.flipkart.flap.thunderingrhino.resources;

import com.sun.jersey.api.client.Client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by suchi.sethi on 17/09/15.
 */
@Path("/services/requestjson")
public class IaasRequestResource {
    private final String iaasURL;

    public IaasRequestResource () {

        this.iaasURL = "http://10.33.65.0:8080/compute/v1/apps/";

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson(@QueryParam("appId") String appId) {

        String url = iaasURL + appId + "/instances?view=summary";

        Client client = Client.create();
        String response = client.resource(url).get(String.class);
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET").entity(response).build();

    }

}

