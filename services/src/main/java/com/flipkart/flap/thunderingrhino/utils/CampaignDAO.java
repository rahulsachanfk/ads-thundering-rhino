package com.flipkart.flap.thunderingrhino.utils;

import com.flipkart.flap.thunderingrhino.entities.CampaignReport;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Iterator;

/**
 * Created by pavan.t on 01/06/15.
 */



@RegisterMapper(CampaignReport.CampaignReportMapper.class)

public interface CampaignDAO {


        @SqlQuery(
                "select  \n" +
                        "seller_name,seller_id,campaign_id,name,status,ox_campaign_id,budget_currency,budget_value,start_date,end_date,rate_id,\n" +
                        "(select r.price  from `ratecard_rates_archived` as r where r.rateid=rate_id)*clicks as usedbudget,\n" +
                        "impressions,clicks,conversions,Revenue,\n" +
                        "(clicks*100)/(impressions) as CTR ,\n" +
                        "if(conversions=0,0, ((conversions*100)/clicks) ) as CVR,\n" +
                        "if(COALESCE(Revenue,0)=0,0,Revenue/\n" +
                        "(\n" +
                        "(select r.price from `ratecard_rates_archived` as r where r.rateid=`rate_id`)\n" +
                        " *clicks\n" +
                        ") \n" +
                        ") as ROI,\n" +
                        "if(COALESCE(Revenue,0)=0,0,((select r.price from `ratecard_rates_archived` as r where r.rateid=`rate_id`)*clicks)/Revenue) as eCPA\n" +
                        "from\n" +
                        "\n" +
                        "(\n" +
                        "select \n" +
                        "seller_name,seller_id,`campaign_id`,name,`status`,`ox_campaign_id`,`budget_currency`,`budget_value`,`rate_id`,`start_date`,`end_date`,`bannerid`,`listing_id`,\n" +
                        "sum(`conversion_value`) as Revenue,\n" +
                        "count(`conversion_value`) as conversions,`impressions`,`clicks`\n" +
                        " from\n" +
                        "(\n" +
                        "select sa.seller_name,sa.seller_id,ca.`campaign_id`,ca.name,ca.`status`,ca.`ox_campaign_id`,ca.`budget_currency`,ca.`budget_value`,ca.`start_date`,ca.`end_date`,ca.`rate_id`,\n" +
                        "fb.`bannerid`,la.`listing_id`,ca2.`conversion_value`,ca2.`conversion_time`,sum(fkd.`impressions`) as impressions,sum(fkd.`clicks`) as clicks\n" +
                        "\n" +
                        "from `sellers_archived` sa\n" +
                        "left join `campaigns_archived` ca\n" +
                        "on sa.id=ca.`advertiser_id`\n" +
                        "left join `fk_banners` fb\n" +
                        "on ca.`ox_campaign_id`=fb.`campaignid`\n" +
                        "left join `listings_archived` la\n" +
                        "on la.`campaign_id`=ca.`campaign_id` and\n" +
                        "la.`ox_banner_id`=fb.`bannerid`\n" +
                        "left join `fk_data_summary_ad_hourly` fkd\n" +
                        "on fkd.`ad_id`=fb.`bannerid`\n" +
                        "left join `conversion_attribution_v2` ca2\n" +
                        "on ca2.`campaign_id`=ca.`ox_campaign_id`\n" +
                        "and ca2.banner_id=fb.`bannerid`\n" +
                        "and ca2.listing_id = la.`listing_id`\n" +
                        "where sa.seller_id=:id\n" +
                        "group by \n" +
                        "sa.seller_name,sa.seller_id,ca.`campaign_id`,ca.name,ca.`status`,ca.`ox_campaign_id`,ca.`budget_currency`,ca.`budget_value`,ca.`start_date`,ca.`end_date`,ca.`rate_id`,fb.`bannerid`,\n" +
                        "la.`listing_id`,ca2.`conversion_value`,ca2.`conversion_time`\n" +
                        ") t\n" +
                        "group by \n" +
                        "seller_name,seller_id,`campaign_id`,name,`status`,`ox_campaign_id`,`budget_currency`,`budget_value`,`start_date`,`end_date`,`rate_id`)  final"
        )
        Iterator<CampaignReport> findSellerReportById(@Bind("id") String id);

}
