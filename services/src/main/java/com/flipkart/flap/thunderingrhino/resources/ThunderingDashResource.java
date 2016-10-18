package com.flipkart.flap.thunderingrhino.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.flap.thunderingrhino.entities.DashboardPojo;
import com.flipkart.flap.thunderingrhino.entities.NewDashboard;
import com.flipkart.flap.thunderingrhino.entities.ProductModules;
import com.flipkart.flap.thunderingrhino.entities.ThunderingDash;
import com.flipkart.flap.thunderingrhino.utils.ThunderingDashDAO;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by suchi.sethi on 21/08/15.
 */
@Path("/services/dashboard")
public class ThunderingDashResource {

    ThunderingDashDAO thunderingDashDAO;

    public ThunderingDashResource(ThunderingDashDAO thunderingDashDAO) {
        this.thunderingDashDAO = thunderingDashDAO;
    }

    @Produces("application/json")
    @Path("/products")
    @GET
    public Response getProductList()throws Exception{

        Map<Integer, String> products = new HashMap<Integer, String>();

        Iterator<Map<Integer,String>> itr= thunderingDashDAO.getProducts();
        while (itr.hasNext()) {
            products.putAll(itr.next());
        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(products).build();
    }

    @Produces("application/json")
    @Path("/modules")
    @GET
    public Response getModuleList() throws Exception{
        List<ProductModules> modules = new ArrayList<>();
        modules = thunderingDashDAO.getModules();
        JSONObject finalobj = new JSONObject();
        JSONArray moduleArr = new JSONArray();

        for(ProductModules module :modules) {
            if (finalobj.has(String.valueOf(module.getProductId()))) {
                moduleArr = finalobj.getJSONArray(String.valueOf(module.getProductId()));
                JSONObject modulejobj = new JSONObject();
                modulejobj.put("moduleid", module.getModuleId());
                modulejobj.put("modulename", module.getModuleName());
                moduleArr.put(moduleArr.length(), modulejobj);

            } else {
                JSONObject modulejobj = new JSONObject();
                modulejobj.put("moduleid", module.getModuleId());
                modulejobj.put("modulename", module.getModuleName());
                moduleArr.put(moduleArr.length(), modulejobj);

            }
            finalobj.put(String.valueOf(module.getProductId()), moduleArr);
            moduleArr = new JSONArray(new ArrayList());
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(finalobj.toString()).build();
    }

    @Produces("application/json")
    @GET
    @Path("/new")
    public Response getNewDashboad() throws Exception{
        List<NewDashboard> nw = new ArrayList<>();
        try{
            nw = thunderingDashDAO.getNewEntry();
        }
        catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        JSONObject finalJson = new JSONObject();
        JSONObject matrices = new JSONObject();
        for(NewDashboard n:nw){
            if (finalJson.has(n.getProductname())){
                JSONObject temp = finalJson.getJSONObject(n.getProductname());
                matrices = getModuleObject(n, temp);
                finalJson.put(n.getProductname(),matrices);
            }
            else {
                    if(n.getModulename()!= null) {
                        matrices = getModuleObject(n, null);
                    }
                    finalJson.put(n.getProductname(), matrices);

            }

        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(finalJson.toString()).build();
    }

    private JSONObject getModuleObject(NewDashboard n, JSONObject jobj){
        JSONArray arrjson = new JSONArray();
        if(jobj!= null){
            if (jobj.has(n.getModulename())) {
                arrjson = jobj.getJSONArray(n.getModulename());
            }
            String url = n.getUrl();
            String display = n.getDisplay();
            String key = n.getDash_key();
            JSONObject j = new JSONObject();
            j.put("display", display);
            j.put("url", url);
            j.put("key", key);
            arrjson.put(arrjson.length(), j);
            jobj.put(n.getModulename(), arrjson);
            return jobj;
        }
        else
        {
            JSONObject modulejson = new JSONObject();
            if (n.getDisplay() != null) {
                String url = n.getUrl();
                String display = n.getDisplay();
                String key = n.getDash_key();
                JSONObject j = new JSONObject();
                j.put("display", display);
                j.put("url", url);
                j.put("key", key);
                arrjson.put(arrjson.length(), j);
            }
            modulejson.put(n.getModulename(),arrjson);
            return modulejson;

        }

    }

    @Produces("application/json")
    @GET

    public Response getdashboradDetails() throws Exception {
        List<ThunderingDash> thunderingDashArrayList = new ArrayList<ThunderingDash>();
        LinkedHashMap<String,LinkedHashMap<String,String>> representation = new LinkedHashMap<String, LinkedHashMap<String,String>>();


        try {

            thunderingDashArrayList = thunderingDashDAO.dashboardEntry();


            for (ThunderingDash thunderingDash : thunderingDashArrayList) {

                LinkedHashMap<String, String> propertieshash = new LinkedHashMap<String, String>();

                propertieshash.put("url", thunderingDash.geturl());
                propertieshash.put("display", thunderingDash.getdisplay());
                representation.put(thunderingDash.getdash_key(), propertieshash);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(representation)
                .build();


    }


    @Path("/insert")
    @GET

    public Response inserttdashboradata(@QueryParam("key") String key, @QueryParam("url") String url, @QueryParam("display") String display) throws Exception {

        try {


            thunderingDashDAO.dashboardinsert(key, url, display);

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }


        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .build();

    }

    @Path("/delete")
    @GET

    public Response deletedashboradata(@QueryParam("key") String key) throws Exception {

        try {


            thunderingDashDAO.dashboarddelete(key);

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .build();

    }

    @Produces("application/json")
    @Path("/new/insert")
    @POST
    @Consumes("application/json")
    public Response insertNewDashboard(DashboardPojo dashboard) throws Exception{
        String msg = "";
        String desh_key = "rah";
        System.out.println("insert api");
        System.out.println("pid" +dashboard.getProductid() +"mid"+ dashboard.getModuleid()+"url"+dashboard.getUrl());
        try {
             desh_key = thunderingDashDAO.getDash_key(dashboard.getDash_key());
        }catch (Exception e){
            System.out.println("catch block");
        }

        if (desh_key != null)
        {
            System.out.println("dash_key");
            msg = "Key already exist, Key should be unique. Prefix with team name. eg. adserver_db, adserver_app";
            return Response.ok()
                    .entity(msg)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "POST")
                    .build();
        }
        if (dashboard.getModuleid().equals("-")){
            System.out.println("asdad");
            try {
                int moduleid = thunderingDashDAO.createModuel(Integer.valueOf(dashboard.getProductid()), dashboard.getNewModuleName());
                thunderingDashDAO.createDashboard(dashboard.getDash_key(), dashboard.getUrl(), dashboard.getDisplay(), moduleid);
                msg = "Inserted Successfully";
            }catch (Exception e){
                return Response.ok().entity("Not inserted, Please try again")
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "POST")
                        .build();
            }
        }
        else {
            try {
                System.out.println("insert dashboard");
                thunderingDashDAO.createDashboard(dashboard.getDash_key(), dashboard.getUrl(), dashboard.getDisplay(), Integer.valueOf(dashboard.getModuleid()));
                msg = "Inserted Successfully";
            }catch (Exception e){
                return Response.ok().entity("Not inserted, Please try again")
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "POST")
                        .build();
            }
        }
        return Response.ok().entity(msg)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST")
                .build();
    }

    @Path("/new/delete")
    @POST
    public Response deletenewdashboradata(@QueryParam("key") String key) throws Exception {
            System.out.println(key);
        try {

            System.out.println(key);
            thunderingDashDAO.deleteDashboard(key);

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok()
                .entity("Deleted Successfully")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST")
                .build();

    }

    @Path("/new/dashboardurl")
    @GET
    public Response getnewdashboradDetails() throws Exception {
        System.out.println("new dashboard");
        List<ThunderingDash> thunderingDashArrayList = new ArrayList<ThunderingDash>();
        LinkedHashMap<String,LinkedHashMap<String,String>> representation = new LinkedHashMap<String, LinkedHashMap<String,String>>();


        try {

            thunderingDashArrayList = thunderingDashDAO.newdashboardEntry();


            for (ThunderingDash thunderingDash : thunderingDashArrayList) {

                LinkedHashMap<String, String> propertieshash = new LinkedHashMap<String, String>();

                propertieshash.put("url", thunderingDash.geturl());
                propertieshash.put("display", thunderingDash.getdisplay());
                representation.put(thunderingDash.getdash_key(), propertieshash);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(representation)
                .build();


    }

}
