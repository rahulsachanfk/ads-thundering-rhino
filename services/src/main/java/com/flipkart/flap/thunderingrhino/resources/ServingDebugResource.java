package com.flipkart.flap.thunderingrhino.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by avaneesh.reddy on 12/10/15.
 */

@Path("/services/servingdebug")
public class ServingDebugResource {

    private final String internalAPIUrl;
    private final String storesUrl;
    private final String debugResponseKeys[] = {"selectedBannersAndListings", "unShownBannersAndListings", "listingPrice", "listingsAfterPacing"};
    private static final String PRODUCT_NOT_SELECTED;
    private static final String LISTING_NOT_SELECTED_FOR_PRODUCT;
    private static final String LISTING_IS_SHOWN;
    private static final String UNABLE_TO_FETCH_PRICE;
    private static final String FILTERED_BY_PACING;


    static {
        PRODUCT_NOT_SELECTED = "The listing is not selected for this request";

        LISTING_NOT_SELECTED_FOR_PRODUCT = new StringBuilder()
                .append("The listing was not selected for its product.\n")
                .append("This could be because\n")
                .append("1. The product is out of stock OR\n")
                .append("2. The listing is inactive due to the related hub being down\n")
                .toString();

        LISTING_IS_SHOWN = "Listing filtered as it has been shown to the user.";

        UNABLE_TO_FETCH_PRICE = "Unable to fetch price for the listing. This is a temporary glitch.";

        FILTERED_BY_PACING = "This listing is part of a campaign which has chosen 'Divide my budget equally'";
    }

    public ServingDebugResource() {
        internalAPIUrl = "http://10.47.1.226:80/v1/advertisements/mapi/select?debug=true";
        storesUrl = "http://10.47.1.159:25290/sherlock/stores/flipkart.com/completion?enable-new-as=true&types=query&q=";
    }

    @POST
    public Response getServingDebugDetails(@FormParam("store") String store, @FormParam("slot") String slot,
                                           @FormParam("query") String query, @FormParam("facet") String facet,
                                           @FormParam("listing") String listingId) {

        JSONObject postInput = null;
        List<Integer> slots = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(slot))
                            .stream().map(Integer::parseInt).collect(Collectors.toList());

        try {
            postInput = getPostInput(store, slots, query, URLDecoder.decode(facet, "UTF-8").replace("p[]=",""));
        } catch (UnsupportedEncodingException e) {
            System.exit(0);
        }

        Client client = Client.create();
        ClientResponse clientResponse = client.resource(internalAPIUrl)
                                                .type(MediaType.APPLICATION_JSON)
                                                .post(ClientResponse.class, postInput.toString());

        if(clientResponse.getStatus() != 200) {
            return Response.status(Response.Status.fromStatusCode(clientResponse.getStatus()))
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET")
                    .entity("Failed: HTTP Status Code: " + Integer.toString(clientResponse.getStatus()))
                    .build();
        }

        JSONObject output = new JSONObject(clientResponse.getEntity(String.class));
        String response = getResponseString(output, listingId, slots);


        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET").entity(response.replace("\n","<br>")).build();
    }

    @GET
    @Path("/getStores")
    public Response getStoresForTitle(@QueryParam("term") String term) {
        JSONArray autocompleteResponse = new JSONArray();
        Client client = Client.create();
        ClientResponse clientResponse = null;
        Set<String> storePaths = new HashSet<>();

        try {
            clientResponse = client.resource(storesUrl + URLEncoder.encode(term, "UTF-8"))
                    .header("X-Flipkart-Client", "ad-platform")
                    .get(ClientResponse.class);
        } catch (Exception e) {}

        if(clientResponse == null || clientResponse.getStatus() != 200) {
            return Response.status(Response.Status.fromStatusCode(500))
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET")
                    .entity(autocompleteResponse.toString())
                    .build();
        }

        JSONArray querySuggestions = new JSONObject(clientResponse.getEntity(String.class))
                                    .getJSONObject("RESPONSE")
                                    .getJSONObject("suggestions")
                                    .getJSONArray("query");

        for(int querySuggestionsIndex = 0; querySuggestionsIndex < querySuggestions.length(); querySuggestionsIndex++) {
            JSONArray primary =  querySuggestions.getJSONObject(querySuggestionsIndex)
                    .getJSONObject("classification").getJSONArray("primary");
            String query = querySuggestions.getJSONObject(querySuggestionsIndex).getString("query");

            for(int primaryIndex = 0; primaryIndex < primary.length(); primaryIndex++) {
                JSONArray primaryItem = primary.getJSONArray(primaryIndex);

                String autocompleteLabel = query + " in " + primaryItem.getJSONObject(primaryItem.length() - 1).getString("title");
                String storeUri = primaryItem.getJSONObject(primaryItem.length() - 1).getJSONObject("resource").getString("uri");

                String autocompleteValue = storeUri.substring(17, storeUri.length() - 7);

                if(storePaths.contains(autocompleteValue)) {
                    continue;
                }

                storePaths.add(autocompleteValue);
                autocompleteResponse.put(new JSONObject("{\"label\":\"" + autocompleteLabel
                        + "\", \"value\":\"" + autocompleteValue
                        + "\", \"queryVal\":\"" + query
                        + "\"}"));
            }
        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .entity(autocompleteResponse.toString())
                .build();
    }

    private String getResponseString(JSONObject output, String listingId, List<Integer> slots) {

        JSONObject debugResponse = output.getJSONObject("debugResponse");
        String productId = getProductIdForListingId(listingId);

        if(debugResponse.getJSONObject("selectionResponse") == null || !(debugResponse.getJSONObject("selectionResponse").toString().contains("\"" + productId + "\""))) {
            return PRODUCT_NOT_SELECTED;
        }

        for(int index = 0; index < debugResponseKeys.length; index++) {
            if(debugResponse.get(debugResponseKeys[index]) == null ||!debugResponse.get(debugResponseKeys[index]).toString().contains("\"" + listingId + "\"")) {
                switch(index) {
                    case 0:
                        return LISTING_NOT_SELECTED_FOR_PRODUCT;
                    case 1:
                        return LISTING_IS_SHOWN;
                    case 2:
                        return UNABLE_TO_FETCH_PRICE;
                    case 3:
                        return FILTERED_BY_PACING;

                }
            }
        }

        StringBuilder responseString = new StringBuilder();

        slots.stream().forEach(slot -> {
            List<Double> relevanceScoreValues = checkRelevanceThreshold(listingId, debugResponse, slot);
            if (relevanceScoreValues.size() > 0) {
                responseString.append(
                        String.format("Listing is not above relevance threshold for slot %d.\n\nProduct Relevance Score = %s.\n\nRelevance Threshold = %s\n\n",
                                slot, relevanceScoreValues.get(0), relevanceScoreValues.get(1)));
            }

            if (!JsonArrayContains(output.getJSONArray("skippedPos"), slot)) {
                JSONArray sponsoredListings = output.getJSONArray("sponsoredListings");
                String shownListing = "";

                for(int i = 0; i < sponsoredListings.length(); i++) {
                    if(sponsoredListings.getJSONObject(i).getInt("position") == slot) {
                        shownListing = sponsoredListings.getJSONObject(i).getJSONObject("adUnit").getString("listingId");
                        break;
                    }
                }

                if (!shownListing.equals(listingId)) {
                    responseString.append("Ad returned for slot " + slot + ".\n Listing ID of ad: "
                            + shownListing
                            + " is ranked higher than given listing.");
                } else {
                    responseString.append("Listing ID of ad returned for slot " + slot + " is same as given listing.");
                }
            }
        });

        if(!responseString.toString().equals("")) {
            return responseString.toString();
        }

        return "Something went wrong! Please try again.";
    }

    private List<Double> checkRelevanceThreshold(String listingId, JSONObject debugResponse, int slot) {
        String productId = getProductIdForListingId(listingId);
        double relevanceThreshold;
        double productRelevanceScore;
        List<Double> scoreValues = new ArrayList<>();

        productRelevanceScore = debugResponse.getJSONObject("selectionResponse").getDouble(productId);

        if(debugResponse.getJSONObject("slotWiseRelevanceThreshold") != null) {
            relevanceThreshold = debugResponse.getJSONObject("slotWiseRelevanceThreshold").getDouble(Integer.toString(slot));
        } else {
            return scoreValues;
        }

        if(productRelevanceScore < relevanceThreshold - 25) {
            scoreValues.add(productRelevanceScore);
            scoreValues.add(relevanceThreshold);
        }
        return scoreValues;
    }

    private JSONObject getPostInput(String store, List<Integer> slots, String query, String facet) {
        JSONObject device = new JSONObject();
        device.put("id", RandomStringUtils.randomAlphabetic(6));
        device.put("adId", RandomStringUtils.randomAlphabetic(6));

        String pageSource = "{\"ctx\":\"search\",\"searchQuery\":\"" + query + "\", \"browseStore\":\"" + store + "\"," +
                "\"facets\":\"" + facet + "\", \"ordering\":\"relevance\",\"offset\": 1}";
        JSONObject page = new JSONObject(pageSource);

        page.put("adslots", slots);

        JSONObject postInput = new JSONObject();
        postInput.put("device", device);
        postInput.put("page", page);
        return postInput;
    }

    private String getProductIdForListingId(String listingId) {
        if(listingId.substring(3, 6).equals("BOK")) {
            return listingId.substring(6, listingId.length() - 6);
        } else {
            return listingId.substring(3, listingId.length() - 6);
        }
    }

    private boolean JsonArrayContains(JSONArray jsonArray, int value) {
        for(int i = 0; i < jsonArray.length(); i++) {
            if(value == jsonArray.getInt(i)) {
                return true;
            }
        }
        return false;
    }

}
