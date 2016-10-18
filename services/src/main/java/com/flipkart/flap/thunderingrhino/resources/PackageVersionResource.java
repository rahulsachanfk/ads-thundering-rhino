package com.flipkart.flap.thunderingrhino.resources;

import com.flipkart.flap.thunderingrhino.utils.HtmlUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Created by jagmeet.singh on 02/01/15.
 */@Path("/services/package-versions")
public class PackageVersionResource {

    private String unknownHost = "{\"unknown\" : \"unknown\"}";

    @GET
    public String getPackageVersions(@QueryParam("url") String urlString) {
        String responseString = "";
        if (urlString != null && !urlString.trim().isEmpty()) {
            responseString = HtmlUtils.getGetResponse(urlString);
        }
        if (responseString == null || responseString.trim().isEmpty()) {
            responseString = unknownHost;
        }

        return responseString;
    }
}
