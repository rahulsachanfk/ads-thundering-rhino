package com.flipkart.flap.thunderingrhino.resources;

import com.flipkart.flap.thunderingrhino.entities.ApplicationDetail;
import com.flipkart.flap.thunderingrhino.utils.ApplicationDAO;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by suchi.sethi on 15/12/15.
 */
@Path("/services/application")
public class ApplicationIDResource {
    ApplicationDAO applicationDAO;

    public ApplicationIDResource(ApplicationDAO applicationDAO) {
        this.applicationDAO = applicationDAO;
    }


    @Produces("application/json")
    @GET

    public Response getApplicationDetails(@QueryParam("app_id") String app_id) throws Exception {
        List<ApplicationDetail> applicationArrayList = new ArrayList<ApplicationDetail>();
        LinkedHashMap<String, List <LinkedHashMap<String,LinkedHashMap<String,String>>>> applicationID = new LinkedHashMap<String, List<LinkedHashMap<String, LinkedHashMap<String, String>>>>();
        List  <LinkedHashMap<String,LinkedHashMap<String,String>>> instanceGrp1 = new ArrayList <LinkedHashMap<String,LinkedHashMap<String,String>>>();


        try {

            applicationArrayList = applicationDAO.applicationEntry(app_id);
            Iterator itr = applicationArrayList.iterator();
            ApplicationDetail applicationDetail = null;
            LinkedHashMap<String, LinkedHashMap<String, String>> instanceGrp = null;
            while (itr.hasNext()) {
                int i = 0;
                applicationDetail = (ApplicationDetail) itr.next();
                instanceGrp = new LinkedHashMap<String, LinkedHashMap<String, String>>();
                LinkedHashMap<String, String> grpValues = new LinkedHashMap<String, String>();
                System.out.println(applicationDetail.getinstance_grp());
                grpValues.put("url", applicationDetail.geturl());
                grpValues.put("port", applicationDetail.getport());

                instanceGrp.put(applicationDetail.getinstance_grp(), grpValues);
                instanceGrp1.add(i, instanceGrp);

                i++;
            }

            applicationID.put(applicationDetail.getapp_id(), instanceGrp1);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        System.out.println(Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(applicationArrayList)
                .build());


        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(applicationID)
                .build();


    }

    @Produces("application/json")
    @GET
    @Path("/appids")
    public Response getAppids(){
        List<ApplicationDetail> applist = new ArrayList<>();
        applist = applicationDAO.getappidList();
        Iterator itr = applist.iterator();
        JSONObject finalobj = new JSONObject();
        while (itr.hasNext()){
            ApplicationDetail appobj= (ApplicationDetail) itr.next();
            if (finalobj.has(appobj.getapp_id())) {
                JSONArray jsonArray = new JSONArray();
                jsonArray = finalobj.getJSONArray(appobj.getapp_id());
                jsonArray.put(jsonArray.length(), appobj.getProduct());
                finalobj.put(appobj.getapp_id(), jsonArray);
            }
            else {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(appobj.getProduct());
                finalobj.put(appobj.getapp_id(),jsonArray);
            }
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(finalobj.toString())
                .build();
    }
}
