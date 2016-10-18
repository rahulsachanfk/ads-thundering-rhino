package com.flipkart.flap.thunderingrhino.configuration;

import com.flipkart.w3.pojo.nps.context.ServiceContext;
import com.flipkart.w3.pojo.nps.payload.ProductServiceRequest;
import com.flipkart.zulu.product.entities.enums.NpsService;
import com.flipkart.zulu.product.entities.enums.NpsView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by pavan.t on 26/05/16.
 */
public class ProductServicePayloadConfig {





    public static Map<String, String> getproductServiceHeaderFields(){
        Map<String, String> productServiceHeaderFields = new HashMap<>();
        productServiceHeaderFields.put("X-Flipkart-Client", "w3.advertising");
        return productServiceHeaderFields;
    }

    public static ProductServiceRequest getProductServiceRequest(){
        ProductServiceRequest productServiceRequestPayload = new ProductServiceRequest();
        ServiceContext serviceContext = new ServiceContext();
        Set<NpsView> npsViewSet = new HashSet<>();
        npsViewSet.add(NpsView.LISTING_INFO);
        npsViewSet.add(NpsView.META_INFO);
        serviceContext.setNpsViews(npsViewSet);

        Set<NpsService> npsServices = new HashSet<>();
        npsServices.add(NpsService.ATHENA);
        serviceContext.setNpsServices(npsServices);

        serviceContext.setOrderListings(true);

        productServiceRequestPayload.setServiceContext(serviceContext);
        return productServiceRequestPayload;
    }

}
