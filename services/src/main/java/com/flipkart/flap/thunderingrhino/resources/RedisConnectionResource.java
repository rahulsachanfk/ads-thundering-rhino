package com.flipkart.flap.thunderingrhino.resources;

import com.flipkart.flap.thunderingrhino.utils.RedisConnection;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.lang.Exception;


/**
 * Created by pavan.t on 07/05/15.
 */


@Path("/redis/")
public class RedisConnectionResource {
    RedisConnection con=null;

    @Path("/test")
    @GET
    public Response checkConnectionTest(@QueryParam("hostname") String hostname,@QueryParam("port") int port) {
        try {
            con= new RedisConnection(hostname,port);
            con.connect();
        }
        catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        finally {
            con.close();
        }

        return Response.ok().build();

    }
}
