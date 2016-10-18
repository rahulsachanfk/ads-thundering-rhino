package com.flipkart.flap.thunderingrhino.resources;

import com.flipkart.flap.thunderingrhino.utils.CMOutboundMessageDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by pushya.gupta on 01/06/16.
 */

@Path("/services/CM")
public class CMOutboundMessageResource {

    CMOutboundMessageDAO cmOutboundMessageDAO;
    public CMOutboundMessageResource(CMOutboundMessageDAO cmOutboundMessageDAO)
    {
        this.cmOutboundMessageDAO = cmOutboundMessageDAO;
    }


    @Path("/{cmp_id}")
    @Produces("application/json")
    @GET
    public Response getMessage(@PathParam("cmp_id") String cmp_id) {
        String message;
        try{
            message = cmOutboundMessageDAO.getMessage(cmp_id);
        }catch(Exception e)
        {   e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();

        }
        return Response.ok()
                .header("Access-Control-Allow-Origin","*")
                .header("Access-Control-Allow-Method","GET")
                .entity(message)
                .build();



    }




}
