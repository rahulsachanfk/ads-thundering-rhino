package com.flipkart.flap.thunderingrhino.resources;

import com.flipkart.flap.thunderingrhino.entities.SherlockListing;
import com.flipkart.flap.thunderingrhino.representations.SherlockProductResponse;
import com.flipkart.flap.thunderingrhino.utils.HtmlUtils;
import com.flipkart.flap.thunderingrhino.utils.SherlockListingsDAO;
import com.sun.jersey.api.client.UniformInterfaceException;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by pavan.t on 19/06/15.
 */

@Path("/services/sherlock")
public class SherlockResource {

    SherlockListingsDAO sherlockListingsDAO;

    public SherlockResource(SherlockListingsDAO sherlockListingsDAO) {
        this.sherlockListingsDAO = sherlockListingsDAO;
    }

    String masterSherlockURL="http://10.32.121.67:25280/solr/next-5/select?q=*:*&fq=id:productId&wt=json";
    String slaveSherlockURL="http://10.47.2.171:25280/solr/next-5/select?q=*:*&fq=id:productId&wt=json";

    @Path("/campaign/{campaignId}")
    @Produces("application/json")
    @GET
    public Response getsherlockDetails(@PathParam("campaignId") String campaignId)  throws Exception{

        ArrayList<SherlockProductResponse> sherlockProductResponseArrayList = new ArrayList<SherlockProductResponse>();


        try {

            Iterator<SherlockListing> sherlockListings = sherlockListingsDAO.findSherlockCampaignById(campaignId);


            while(sherlockListings.hasNext()){
                SherlockListing sherlockListing = new SherlockListing();
                sherlockListing = sherlockListings.next();

                String Product_Id=sherlockListing.getProduct_Id();
                int Suppressed = sherlockListing.getSupressed();

                String MasterSherlockURL= this.masterSherlockURL.replaceAll("productId", Product_Id);
                String slaveSherlockURL = this.slaveSherlockURL.replaceAll("productId", Product_Id);
                String masterResponse = HtmlUtils.getGetResponse(MasterSherlockURL);
                String slaveResponse = HtmlUtils.getGetResponse(slaveSherlockURL);



                JSONObject MasterjsonObj = new JSONObject(masterResponse);
                JSONObject SlavejsonObj = new JSONObject(slaveResponse);

                int MasterFound = MasterjsonObj.getJSONObject("response").getInt("numFound");
                int SlaveFound = SlavejsonObj.getJSONObject("response").getInt("numFound");

                SherlockProductResponse sherlockProductResponse = new SherlockProductResponse();
                sherlockProductResponse.setProduct_Id(Product_Id);
                sherlockProductResponse.setSuppressed(Suppressed);
                sherlockProductResponse.setSlaveFound(SlaveFound);
                sherlockProductResponse.setMasterFound(MasterFound);
                sherlockProductResponseArrayList.add(sherlockProductResponse);

            }


        } catch (UniformInterfaceException e) {
            e.printStackTrace();
            //return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(sherlockProductResponseArrayList)
                .build();
    }


}
