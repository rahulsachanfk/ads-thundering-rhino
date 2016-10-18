package com.flipkart.flap.thunderingrhino.misc;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import com.flipkart.flap.thunderingrhino.configuration.ProductServiceDatasource;
import com.flipkart.flap.thunderingrhino.entities.Product;
import com.flipkart.flap.thunderingrhino.exceptions.DatasourceException;
import com.flipkart.w3.common.utils.ProductService;
import com.flipkart.w3.pojo.nps.context.ServiceContext;
import com.flipkart.w3.pojo.nps.payload.ProductServiceRequest;
import com.flipkart.zulu.product.entities.enums.NpsService;
import com.flipkart.zulu.product.entities.enums.NpsView;
import com.google.common.collect.Lists;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.util.*;

/**
 * Created by pavan.t on 29/02/16.
 */

public class ProductListingInformation {
    public static ProductServiceDatasource newViewDatasource;
    public static ProductService productService;
    public static void main(String[] args) {

        Map<String, String> productServiceHeaderFields = new HashMap<>();
        productServiceHeaderFields.put("X-Flipkart-Client", "w3.advertising");

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

        newViewDatasource = new ProductServiceDatasource(new ProductService("http://10.33.106.109:8190/productService/getCustomProductDetails", productServiceHeaderFields, productServiceRequestPayload));



         productService = new ProductService("http://10.33.106.109:8190/productService/getCustomProductDetails", productServiceHeaderFields, productServiceRequestPayload);

        /*
        List<String> productIds = new ArrayList<String>();
        productIds.add("SRTE86J6HYZRYGKW");

        for (String productId : productIds) {
            Product product = null;
            try {
                product = newViewDatasource.fromProductId(productId, "LSTSRTE86J6HYZRYGKWKGLFLG");
            } catch (DatasourceException e) {
                e.printStackTrace();
            }
            System.out.println("Listing Id : LSTSRTE86J6HYZRYGKWKGLFLG" + "Availability : " + product.isAvailable());
        }
        */

        ProductListingInformation productListingInformation = new ProductListingInformation();

        productListingInformation.testExistenceOfListingDataInNPS();

    }


    public void testExistenceOfListingDataInNPSUsingCustomView(){
        List<String> listingIds = Lists.newArrayList("LSTSHOE6G4UYZWMXAMG45VW23",
                "LSTSHOE6GAN7H5EHYFYQ5DEJP","LSTSHOE6GANAHPENTRGPLPM0G","LSTSHOE6GANF8X6EEPHGY80HA","LSTSHOE6GANFCTFNNYSMSCKTF","LSTSHOE6GANQQCAGDWV03VGSH","LSTSHOE6GANUHJA2FFQP9LUKQ","LSTSHOE6GANYHGABJ8M9BX6H8","LSTSHOE6GAWHWKR5ZHFBVIGDK","LSTSHOE6GAWV7YJYMHWAJVLBZ","LSTSHOE6GAXKRZMQDR6FXSILV","LSTSHOE7CHXQF2964YHNXDP2Z","LSTSHOE7CHYBEAXRKT43SXLSC","LSTSHOE7CHYD9ZUZC8HKKO0CZ","LSTSHOE7CHYPJFVJRZQCDMFJR","LSTSHOE7CHYTZXRYEFWG4GGLD","LSTSHOE7H5K2G8MD92VTG3LL1","LSTSHOE7H5K5NUASHGAOLRYMK","LSTSHOE7H5K5QHQZHD7XI3E8M","LSTSHOE7H5K6RHVGJQKQIOBYT","LSTSHOE7H5K9GQ9U929SJNX5X","LSTSHOE7H5KAE5DG8PXCY3PHX","LSTSHOE7H5KB9H76UCFPSVLC8","LSTSHOE7H5KBEUUVWAKGXCHP3","LSTSHOE7H5KC3TBRZPMWZ4S7S","LSTSHOE7H5KCQC8GMZDG6JCDX","LSTSHOE7H5KE7UDQEHROEVWZL","LSTSHOE7H5KE8BPKJDYBWUOOK","LSTSHOE7H5KEDHGKX3H07NPCD","LSTSHOE7H5KF8A9BCXUYSLWSC","LSTSHOE7H5KFECZZGNZZRBBIX","LSTSHOE7H5KFNQK3PHM8FY4MM","LSTSHOE7H5KGHW2UPJNHCAT1M","LSTSHOE7H5KGMUYMGXX6FGA3H","LSTSHOE7H5KGTR4WQ4EV1N7NS","LSTSHOE7H5KGZZHAFQAUMCISC","LSTSHOE7H5KHUHQYQAVVUQLGA","LSTSHOE7H5KJ9NZFA8BW5IURZ","LSTSHOE7H5KMFHHQYH2VIZG5C","LSTSHOE7H5KMFJGGHGGIPR8HC","LSTSHOE7H5KN4BCMEFZAECUO5","LSTSHOE7H5KQQJCWCV50ULRRX","LSTSHOE7H5KSGFHNUS9G59E8Q","LSTSHOE7H5KSZHJ6GZXTB9GAR","LSTSHOE7H5KUFCATRPZIQRTDI","LSTSHOE7H5KUWSBU2CVOHBRAC","LSTSHOE7H5KUYCFQZH2QSEEMS","LSTSHOE7H5KVUBZRZKHXN8ZXZ","LSTSHOE7H5KXHHVBYRAHNRUQJ","LSTSHOE7H5KXP8GNFZXZO4L4A","LSTSHOE7H5KZNVDEUCHMGS6XE","LSTSHOE9DHT2HARHZQGEW3OJO","LSTSHOE9DHT3YTHESHC4ZHBPN","LSTSHOE9DHT5KF8ZZP6GKB94D","LSTSHOE9DHT64XYGQQABJB3KU","LSTSHOE9DHT658KQ3ENH0BLDZ","LSTSHOE9DHT7HDSDWRMY5RHAI","LSTSHOE9DHTDV2W9T7TOUJP9N","LSTSHOE9DHTDZRFWCZ2YR4MT5","LSTSHOE9DHTEQX9GBHETA6W6W","LSTSHOE9DHTEWUWUGQNMH8VT5","LSTSHOE9DHTG2HRRHZYGHPYND","LSTSHOE9DHTGB223SATUV2LZH","LSTSHOE9DHTGGBRUARGV2XLPR","LSTSHOE9DHTHJHSZSN4CGBRNG","LSTSHOE9DHTPK3C2JHJWAT2WN","LSTSHOE9DHTPZABG8YWVRVDQX","LSTSHOE9DHTPZVRDSKAM4FD6B","LSTSHOE9DHTQEU3B444EYADYZ","LSTSHOE9DHTSMEBQSSQG3GK9V","LSTSHOE9DHTV4YVBTEHFCQGRJ","LSTSHOE9DHTWTV2QGHWJEV2KL","LSTSHOE9DHTXVZZPDM5PRMDVH","LSTSHOE9DHTXYTU8ZWMZ1OLS9","LSTSHOE9DHTXZYU8E2G3RJBC6","LSTSHOE9DHTY459SYNSDYOAJC","LSTSHOE9DHTYVUH9CV45X3KJB","LSTSHOE9DHTZAZGKHZHGSJ7I1","LSTSHOE9DHTZJDHHXTVSJKKGR","LSTSHOE9DHU23P9UHQZ2WYGHJ","LSTSHOE9DHU2SYN9NRW7BLMNF","LSTSHOE9DHU58UVMXXK1QD87F","LSTSHOE9DHUEG6GWVURMUBXNY","LSTSHOE9DHUGDZMP7GGBKTTD4","LSTSHOE9DHUHNK393NMITGWCP","LSTSHOE9DHUHQQUZZRGNUBSVG","LSTSHOE9DHUSGTT9BG7KUKSTE","LSTSHOE9DHUUZ4P4YX8XTEPOU","LSTSHOE9DHUZGQXNSGS1PR0CB","LSTSHOE9DHUZXRMFFNNKDDKZS","LSTSHOE9DW77NQWFKVHRX07II","LSTSHOE9DW7A5XGGED7R0DHVU","LSTSHOE9DW7HGMXDHUVNQPPCE","LSTSHOE9DW7HNVDWZCBDFYAVK","LSTSHOE9DW7MZTGNJAFXWTYUT","LSTSHOE9DW7VESGEQHVEAKQCO","LSTSHOE9GWS7GGHFF8FJLX9US","LSTSHOE9GWSNRB74THPZLPQBD","LSTSHOE9GWSTTHHZPZW9QVZQZ","LSTSHOE9GWT3HJM6GMQAHUAD6","LSTSHOE9GWT6MCUH3R232VBLH","LSTSHOE9GWTAPVEAG3BG1GGTR","LSTSHOE9GWTBZUSWBSDKAXCOU","LSTSHOE9GWTEGSWR3FH3YH8JA","LSTSHOE9GWTFCAEHZTKHXU2GM","LSTSHOE9GWTFGHC2QU3SZNOXE","LSTSHOE9GWTK9UN3JJENLXTLD","LSTSHOE9GWTMDGGGRVNGAWXKY","LSTSHOE9GWTQFYAVK8ZLJQC3M","LSTSHOE9GWTQPQHKB7ZMZ6NFR","LSTSHOE9GWTQVWMETRTDWKEOQ","LSTSHOE9GWTUMVRJNXGEXH5PO","LSTSHOE9GWTWUXGEY6GJKNK3S","LSTSHOE9GWTYZBFTYGCHL7GM0","LSTSNDE7HGY6RVUY4BP2FYETU","LSTSNDE7HGY9ZRKYZ9GIP24GL","LSTSNDE7HGYC9ZJFTZZYQ5860","LSTSNDE7HGYG6TV9RJVHBZI95","LSTSNDE7HGYW5SDJWYGFTUQQP","LSTSNDE9ENYBF5AHTVKCXG302","LSTSNDE9ENYH9ESB4CDGBMVV7","LSTSNDE9ENYSTQHQVARTDL9IO","LSTSNDE9ENYZ4H3PYTFOOUG23","LSTSNDE9ENYZAHRHFZYX2I8IJ");
        for (String singleListing : listingIds) {
            try {
                Product currProduct  = this.newViewDatasource.fromProductId(ProductServiceDatasource.getProductId(singleListing), singleListing);


                if (currProduct != null){
                    System.out.println(String.format("Listing id: %s, is available: %b", currProduct.getReferenceId(), currProduct.isAvailable()));
                }
                else{
                    System.out.println("!!!!!!!!! Data not found for listing id: " + singleListing);
                }
            } catch (DatasourceException e) {
                System.out.println("!!!!!!!!! Data not found for listing id: " + singleListing);
                e.printStackTrace();
            }
        }
    }

    public void testExistenceOfListingDataInNPS(){

        List<String> listingIds = Lists.newArrayList("LSTCOMECAZDCGJF3KNKTLZ088",
                "LSTCOME7V3XSMECAUCBI7LNZK",
                "LSTCOME7V8PC6YSYZPQTJRISV",
                "LSTCOME8FFPZ6EACGZ2KG8WLM",
                "LSTCOMEDGFVSKAYT7KGO9UYBL",
                "LSTCOMEAKUXCVZBDMVVEKNZW",
                "LSTCOMEDFBAE526M426LSSTY5",
                "LSTCOMEEFFDQ7QCRXQQWW9UE",
                "LSTCOME7SU4FPKQCHVAMLJ7SL",
                "LSTCOME9HET934GHWBPKP4BBG",
                "LSTCOMDYG3QBQSDKWQMLSYQI",
                "LSTCOME8FFPVHKACMVBDQWOM",
                "LSTCOMEDFBAYREPUC6E9MMZ9N",
                "LSTCOMEDCVKFJZGC5DZNA9SUX",
                "LSTCOMEAZ94GT9UZYGGD5YPPR",
                "LSTCOMEAE7XDPMSAMRWBYCYN",
                "LSTCOME7U2JZJNJAVDH0X7SDC",
                "LSTCOME7U2JZT8S3ZFKLFNGEU",
                "LSTCOMEEFFDM4YTFUSHJXHLBQ",
                "LSTCOMEDCVKGGSKXZB3F0DLLZ",
                "LSTCOMEDGFVC8XWQMJGNCD83",
                "LSTCOMEBBDZUTTFJ78FZ9J0XV"
                );

        for (String singleListing : listingIds) {
            try {
                Product currProduct  = this.newViewDatasource.fromProductId(ProductServiceDatasource.getProductId(singleListing), singleListing);
                System.out.println(ProductServiceDatasource.getProductId("PEREYQBJUFKA9VC7"));

                System.out.println(productService.getProductDetails("PEREYQBJUFKA9VC7"));


                System.out.println(currProduct.getSeller());
                if (currProduct != null){
                    System.out.println(String.format("%s"+","+"%b", currProduct.getReferenceId(), currProduct.isAvailable()));
                }
                else{
                    System.out.println(singleListing+","+"false");
                }
            } catch (DatasourceException e) {
                System.out.println(singleListing+","+"false");
                e.printStackTrace();
            }
        }
    }


    /*
    ProductServiceDatasource providerServiceProvider(){
        ProductServiceRequest productServiceRequestPayload = new ProductServiceRequest();
        ServiceContext serviceContext = new ServiceContext();
        Set<NpsView> npsViewSet = new HashSet<>();
        npsViewSet.add(NpsView.LISTING_INFO);
        npsViewSet.add(NpsView.META_INFO);

        Set<NpsService> npsServices = new HashSet<>();
        npsServices.add(NpsService.ATHENA);

        serviceContext.setNpsViews(npsViewSet);
        serviceContext.setNpsServices(npsServices);
        productServiceRequestPayload.setServiceContext(serviceContext);

        ProductServiceDatasource productServiceDatasource = new ProductServiceDatasource(
                new ProductService(productServiceUrl, this.productServiceClientHeaders, productServiceRequestPayload,
                        productServiceConnectTimeout,
                        productServiceReadTimeout));

        return productServiceDatasource;
    }
    */





    private Client providesJerseyClient() {
        ClientConfig defaultConfig = new DefaultClientConfig();
        defaultConfig.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", Boolean.TRUE);
        defaultConfig.getClasses().add(JacksonJsonProvider.class);
        Client jerseyClient = Client.create(defaultConfig);
        jerseyClient.setConnectTimeout(Integer.valueOf(0));
        jerseyClient.setReadTimeout(Integer.valueOf(0));
        return jerseyClient;
    }
}
