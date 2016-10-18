package com.flipkart.flap.thunderingrhino;

import com.flipkart.flap.thunderingrhino.configuration.ProductServicePayloadConfig;
import com.flipkart.flap.thunderingrhino.configuration.ServicesConfiguration;
import com.flipkart.flap.thunderingrhino.entities.CampaignListingReport;
import com.flipkart.flap.thunderingrhino.resources.*;
import com.flipkart.flap.thunderingrhino.services.BucketReportService;
import com.flipkart.flap.thunderingrhino.utils.*;
import com.flipkart.w3.common.utils.ProductService;
import com.sun.jersey.api.client.Client;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by jagmeet.singh on 02/01/15.
 */
public class ServicesApplication extends Application<ServicesConfiguration> {



    public static void main(String[] args) throws Exception {
        new ServicesApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ServicesConfiguration> bootstrap) {

        
    }

    @Override
    public String getName() {
        return "Package Versions";
    }

    @Override
    public void run(ServicesConfiguration configuration, Environment environment) throws Exception {

        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        final DBI rejdbi = factory.build(environment, configuration.getReportDataSourceFactory(), "mysql2");
        final DBI camjdbi = factory.build(environment, configuration.getCamDataSourceFactory(), "mysql3");
        final DBI anyjdbi = factory.build(environment, configuration.getAnyDataSourceFactory(), "mysql4");
        final DBI madijdbi = factory.build(environment, configuration.getMadiDataSourceFactory(), "mysql5");
        final SellerDAO sellerDAO = camjdbi.onDemand(SellerDAO.class);
        final CampaignDAO campaignDAO = camjdbi.onDemand(CampaignDAO.class);
        final SherlockListingsDAO sherlockListingsDAO = camjdbi.onDemand(SherlockListingsDAO.class);
        final CampaignAuditDAO campaignAuditDAO = camjdbi.onDemand(CampaignAuditDAO.class);
        final CampaignListReportDAO campaignListReportDAO = rejdbi.onDemand(CampaignListReportDAO.class);
        final ThunderingDashDAO thunderingDashDAO = jdbi.onDemand(ThunderingDashDAO.class);
        final RelevanceTaggingDAO relevanceTaggingDAO = camjdbi.onDemand(RelevanceTaggingDAO.class);
        final ApplicationDAO applicationDAO = jdbi.onDemand(ApplicationDAO.class);
        final BucketReportDAO bucketReportDAO = camjdbi.onDemand(BucketReportDAO.class);
        BucketReportResource bucketReportResource = new BucketReportResource(new BucketReportService(bucketReportDAO));
        environment.jersey().register(bucketReportResource);
        final CMOutboundMessageDAO cmOutboundMessageDAO = camjdbi.onDemand(CMOutboundMessageDAO.class);
        final MadisonMessageDAO madisonMessageDAO = madijdbi.onDemand(MadisonMessageDAO.class);
        final ScheduledExecutorService scheduledExecutorService = environment.lifecycle().scheduledExecutorService("slack-notify").threads(1).build();

        String datajsonDirectory = configuration.getSlackFilePath();
        String datajsonfile = datajsonDirectory+"/"+"slack_data_json.txt";

        scheduledExecutorService.scheduleAtFixedRate(new ServerStatusFlip(datajsonfile), 5, 1, TimeUnit.MINUTES);


        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);

        final JedisPool pool = new JedisPool(poolConfig,configuration.getRedisHost(), Integer.parseInt(configuration.getRedisPort()));


        // Only create an instance once
        final Client jerseyClient = Builders.getJerseyClient();

        final PackageVersionResource packageVersionResource = new PackageVersionResource();
        environment.jersey().register(packageVersionResource);

        final RedisConnectionResource redisConnectionResource = new RedisConnectionResource();
        environment.jersey().register(redisConnectionResource);

        final ThunderingDashResource thunderingDashResource = new ThunderingDashResource(thunderingDashDAO);
        environment.jersey().register(thunderingDashResource);

        final ApplicationIDResource applicationidResource  = new ApplicationIDResource(applicationDAO);
        environment.jersey().register(applicationidResource);

        final TickerDisplayResource tickerDisplayResource = new TickerDisplayResource(rejdbi.open(),anyjdbi.open(),pool);
        environment.jersey().register(tickerDisplayResource);

        final SherlockResource sherlockResource = new SherlockResource(sherlockListingsDAO);
        environment.jersey().register(sherlockResource);

        final CampaignAuditResource campaignAuditResource = new CampaignAuditResource(campaignAuditDAO, campaignListReportDAO, camjdbi.open());
        environment.jersey().register(campaignAuditResource);

        final IaasRequestResource iaasRequestResource = new IaasRequestResource();
        environment.jersey().register(iaasRequestResource);

	final ListingIdResource listingIdResource = new ListingIdResource();
        environment.jersey().register(listingIdResource);

        final RelevanceTaggingResource relevanceTaggingResource = new RelevanceTaggingResource(relevanceTaggingDAO);
        environment.jersey().register(relevanceTaggingResource);

        final CMOutboundMessageResource cmOutboundMessageResource = new CMOutboundMessageResource(cmOutboundMessageDAO);
       environment.jersey().register(cmOutboundMessageResource);

       final MadisonMessageResource madisonMessageResource = new MadisonMessageResource(madisonMessageDAO);
        environment.jersey().register(madisonMessageResource);

        final ServingDebugResource servingDebugResource = new ServingDebugResource();
        environment.jersey().register(servingDebugResource);


        ProductService productService = new ProductService(configuration.getNpsProductVipURL(), ProductServicePayloadConfig.getproductServiceHeaderFields(),ProductServicePayloadConfig.getProductServiceRequest());
        final NpsProductResource npsProductResource = new NpsProductResource(productService);
        environment.jersey().register(npsProductResource);


        final ServicesHealthCheck servicesHealthCheck = new ServicesHealthCheck(configuration.getHealthCheckProperty());
        environment.healthChecks().register("configurationCheck", servicesHealthCheck);

        if (configuration.getCampaignDetailsAPIFlipper().isEnabled()) {
            final CampaignDetailsResource campaignDetailsResource = new CampaignDetailsResource(
                    Builders.getExternalAPIClient(configuration, jerseyClient),camjdbi.open());
            environment.jersey().register(campaignDetailsResource);
        }
    }
}
