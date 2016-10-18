package com.flipkart.flap.thunderingrhino.resources;

import com.flipkart.flap.thunderingrhino.clients.ExternalAPIClient;
import com.flipkart.flap.thunderingrhino.entities.ProductRequest;
import com.flipkart.w3.common.utils.ProductService;
import com.sun.jersey.api.client.UniformInterfaceException;
import lombok.AllArgsConstructor;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.StringMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Map;

/**
 * Created by pavan.t on 26/05/16.
 */

@AllArgsConstructor
@Path("/services/product")
public class NpsProductResource {
        private ProductService productService;

        @Path("/npsproduct")
        @Produces("application/json")
        @POST
        public Response getProductDetails(ProductRequest productRequest) {
                      String[] productIds = new String[productRequest.getProductIds().size()];
            Collection<Map<String, Object>> productDetails = this.productService.getProductDetails(productRequest.getProductIds().toArray(productIds));

            try {
                if(productDetails==null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            } catch (UniformInterfaceException e) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.ok()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET")
                    .entity(productDetails)
                    .build();
        }
    }


