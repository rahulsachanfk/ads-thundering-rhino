package com.flipkart.flap.thunderingrhino.configuration;


import com.flipkart.flap.thunderingrhino.entities.Product;
import com.flipkart.flap.thunderingrhino.exceptions.DatasourceException;
import com.flipkart.flap.thunderingrhino.exceptions.ProductAttributeException;
import com.flipkart.flap.thunderingrhino.exceptions.ProductNotFoundException;
import com.flipkart.flap.thunderingrhino.utils.Tuple;
import com.flipkart.w3.common.utils.ProductService;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dipanjan Mukherjee (dipanjan.mukherjee@flipkart.com)
 * Date: 4/6/14
 */
public class ProductServiceDatasource {
    private static Logger logger = LoggerFactory.getLogger(ProductServiceDatasource.class);

    private ProductService service;
    private static final String PRODUCT_ID_KEY = "id";
    private static final String BASE_INFO_KEY = "baseInfo";
    private static final String META_INFO_KEY = "metaInfo";
    private static final String LISTING_INFO_KEY = "listingInfo";
    private static final String LISTINGS_KEY = "listings";
    private static final String ATTRIBUTE_KEY = "attributes";
    private static final String IS_DISCOVERABLE_KEY = "is_discoverable";
    private static final String PRODUCT_STATE_KEY = "product_state";
    private static final String LISTING_STATUS_KEY = "listingStatus";
    private static final String IS_LIVE_KEY = "is_live";
    private static final String VERTICAL_KEY = "vertical";
    private static final String SHOW_MRP_KEY = "show_mrp";
    private static final String IN_STOCK_KEY = "is_available";
    private static final String LISTING_STATE_KEY = "listing_state";
    private static final String LISTING_ID_KEY = "listing_id";
    private static final String TITLE_KEY = "titles";
    private static final String W3_TITLE_KEY = "w3_title";
    private static final String PRICE_KEY = "mrp";
    private static final String SELLING_PRICE_KEY = "fsp";
    private static final String SANTA_OFFER_KEY = "price_cut";
    private static final String SANTA_OFFER_PRICE_KEY = "priceAfterDiscount";
    private static final String CLICK_IDENTIFIER_NAME = "flapclick";
    private static final String CLICK_IDENTIFIER_VALUE = "true";

    public ProductServiceDatasource(ProductService service) {
        this.service = service;
    }
   public BigDecimal getEffectivePrice(Map<String, Object> preferredListing) {
        BigDecimal sellingPrice;
        BigDecimal santaOfferPrice = getListingSantaPrice(preferredListing);
        sellingPrice = (santaOfferPrice == null) ? getListingPrice(preferredListing, SELLING_PRICE_KEY) : santaOfferPrice;
        return sellingPrice;
    }

    private boolean doesPreferredSellerHaveStock(Map<String, Object> listing) {
        return Boolean.valueOf((Boolean) listing.get(IN_STOCK_KEY));
    }

    private boolean getPreferredSellerShowMrp(Map<String, Object> listing ) {
        return Boolean.valueOf((Boolean) listing.get(SHOW_MRP_KEY));
    }

    private BigDecimal getListingPrice(Map<String, Object> listing, String priceKey){
        return new BigDecimal(String.valueOf(listing.get(priceKey)));
    }

    private BigDecimal getListingSantaPrice(Map<String, Object> listing) {
        Map<String, Object> santaOffer = (Map<String, Object>) listing.get(SANTA_OFFER_KEY);
        if(santaOffer == null) {
            return null;
        } else {
            return new BigDecimal(String.valueOf(santaOffer.get(SANTA_OFFER_PRICE_KEY)));
        }
    }

    private String selectPreferredSellerName(Map<String,Object> listings, String preferredListingId) {
        Map<String, Object> listing = (Map<String, Object>) listings.get(preferredListingId);
        Preconditions.checkNotNull(listing, "ListingId %s not found for this product", preferredListingId);
        Map<String, Object> sellerInfo = (Map<String, Object>) listing.get("sellerInfo");

        if(sellerInfo != null) {
            Map<String, Object> sellerProfile = (Map<String, Object>) sellerInfo.get("sellerProfileInfo");
            if (sellerProfile != null) {
                return String.valueOf(sellerProfile.get("displayName"));
            }
        }
        return null;
    }

    /* New view functions */

    public List<Product> getAllListingDetails(List<Tuple<String, String>> productIdListingIdTupleList) throws ProductAttributeException {

        /**
         * NPS can be queried only through product id
         * If listing id data is required, its data must be fetched through its product id
         * Data for a product id from NPS contains data for all of its listings
         */
        Set<String> productIds = new HashSet<>();  //product ids to fetch data from NPS
        Map<String, List<String>> productToListingIdsMap = new HashMap<>();
        List<Product> listingDetails = new ArrayList<>();

        //Create mapping from productId -> List of listingId
        productIdListingIdTupleList.stream().forEach(singleTuple -> {
            String currentProductId = singleTuple.getA();
            productIds.add(currentProductId);
            if (productToListingIdsMap.get(currentProductId) == null) {
                productToListingIdsMap.put(currentProductId, new ArrayList<>());
            }
            productToListingIdsMap.get(currentProductId).add(singleTuple.getB()); //Add this listing id to current product id
        });

        //Get details for all the products
        Collection<Map<String, Object>> productDetailsCollection = this.service.getProductDetails(productIds.toArray(new String[productIds.size()]));

        //Fetch listings data from product id data
        Product currentListing = null;
        for(Map<String, Object> singleProductDetail : productDetailsCollection){
            LinkedHashMap<String, Object> metaInfo = (LinkedHashMap<String, Object>) singleProductDetail.get(META_INFO_KEY);
            if(metaInfo != null) {
                String currentProductId = (String) metaInfo.get(PRODUCT_ID_KEY);
                List<String> listingIdsForCurrentProductId = productToListingIdsMap.get(currentProductId);

                for (String listingId : listingIdsForCurrentProductId) {
                    currentListing = createProductFromDetails(singleProductDetail, listingId);
                    if (currentListing != null) {
                        listingDetails.add(currentListing);
                    }
                }
            }
        }
        return listingDetails;
    }

    /**
     * Returns information about listing from product details
     * @param productDetails
     * @param overrideListingId
     * @return If given listing data is not found in product information, null is returned
     * @throws ProductAttributeException
     */
    private Product createProductFromDetails(Map<String, Object> productDetails, String overrideListingId) throws ProductAttributeException {
        Product product = null;
        String productId = null;

        if (productDetails != null) {
            try {
                LinkedHashMap<String, Object> baseInfo = (LinkedHashMap<String, Object>) productDetails.get(BASE_INFO_KEY);
                LinkedHashMap<String, Object> metaInfo = (LinkedHashMap<String, Object>) productDetails.get(META_INFO_KEY);
                LinkedHashMap<String, Object> listingInfo = (LinkedHashMap<String, Object>) productDetails.get(LISTING_INFO_KEY);

                productId = (String) metaInfo.get(PRODUCT_ID_KEY);
                Boolean isDiscoverable = null;
                String productState = null;

                if(metaInfo != null) {
                    isDiscoverable = (Boolean) metaInfo.get(IS_DISCOVERABLE_KEY);
                    productState = (String) metaInfo.get(PRODUCT_STATE_KEY);
                    logger.debug("For product {} isDiscoverable is {} , and productState is {}", productId, isDiscoverable, productState);

                    List<Map<String, Object>> listings = null;
                    if (listingInfo != null) {
                        listings = (ArrayList<Map<String, Object>>) listingInfo.get(LISTINGS_KEY);
                    }
                    String listingId = overrideListingId;

                    if (listings != null) {
                        Map<String, Object> preferredListing = getPreferredListing(listings, listingId);
                        if (preferredListing != null) {
                            Object listingAttributesObject = preferredListing.get(ATTRIBUTE_KEY);

                            Preconditions.checkArgument(listingAttributesObject instanceof Map,
                                    "Listing Attribute object is absent for product: %s and listing: %s", productId, listingId);

                            String listingState = (String) preferredListing.get(LISTING_STATE_KEY);
                            String listingStatus = (String) preferredListing.get(LISTING_STATUS_KEY);
                            Map<String, Object> listingAttributes = (Map<String, Object>) listingAttributesObject;
                            Boolean isLive = (Boolean) listingAttributes.get(IS_LIVE_KEY);

                            logger.debug("For product {} and listing {} listingState is {} and isLive is {} ", productId, listingState, listingId, isLive);

                            String sellerName = this.selectPreferredSellerName(preferredListing);
                            String uri = "http://www.flipkart.com" + (String) metaInfo.get("url");
                            uri = UriBuilder.fromUri(uri)
                                    .queryParam(CLICK_IDENTIFIER_NAME, CLICK_IDENTIFIER_VALUE)
                                    .build()
                                    .toString();

                            BigDecimal sellingPrice = getEffectivePrice(preferredListing);
                            String vertical = String.valueOf(metaInfo.get(VERTICAL_KEY));

                            String title = null;
                            if (baseInfo != null) {
                                Map<String, Object> titles = (Map<String, Object>) baseInfo.get(TITLE_KEY);
                                title = (String) titles.get(W3_TITLE_KEY);
                            }

                            product = new Product(
                                    title,
                                    sellingPrice,
                                    getListingPrice(preferredListing, SELLING_PRICE_KEY),
                                    getListingSantaPrice(preferredListing),
                                    getListingPrice(preferredListing, PRICE_KEY),
                                    sellerName,
                                    new URL(uri),
                                    vertical,
                                    listingId,
                                    doesPreferredSellerHaveStock(preferredListing),
                                    listingState,
                                    getPreferredSellerShowMrp(preferredListing),
                                    productState,
                                    isDiscoverable == null ? false : isDiscoverable,
                                    isLive == null ? false : isLive,
                                    listingStatus
                            );
                        } else {
                            logger.debug("Listing id:{} not found for details from NPS for product id:{}. Returning null product.", listingId, productId);
                        }
		   } else {
                        logger.debug("No listings available for product id:{}. Returning null product.", productId);
                    }
                }
            } catch (MalformedURLException e) {
                throw new ProductAttributeException("Couldn't compute image URL for: " + productId, e);
            }
        }
        return product;
    }

    /**
     * Returns product for given listing id provided.Since there is no preferred listing Id in new view,
     * it is mandatory to provide listing id with product Id
     * @param productId
     * @param overrideListingId
     * @return
     * @throws DatasourceException
     */

    public Product fromProductId(String productId, String overrideListingId) throws DatasourceException {
        Map<String, Object> details;
        final Product product;

        try {
            details = this.service.getProductDetails(productId);
        } catch (Exception e) {
            throw new ProductNotFoundException(productId, e);
        }

        product = createProductFromDetails(details, overrideListingId);
        return product;
    }

    /**
     * Returns listing info for a given listing id
     */
    private Map<String, Object> getPreferredListing(List <Map<String,Object>> listings, String listingId) {
        for(Map<String,Object>listing: listings){
            String currListingId = (String)listing.get(LISTING_ID_KEY);
            if((listingId.equals(currListingId))){
                return listing;
            }
        }
        return null;
    }

    private String selectPreferredSellerName(Map<String,Object> preferredListing) {
        Map<String, Object> sellerInfo = (Map<String, Object>) preferredListing.get("seller_info");
        if(sellerInfo != null) {
            Map<String, Object> sellerProfile = (Map<String, Object>) sellerInfo.get("sellerProfileInfo");
            if(sellerProfile != null)
                return String.valueOf(sellerProfile.get("display_name"));
        }
        return null;
    }

    public static String getProductId(String listingId) {
        int length = listingId.length();
        String productId = listingId.substring(3, length - 6);

        if (productId.startsWith( "BOK")) {
            return productId.substring(3);
        }
        return productId;
    }
}
