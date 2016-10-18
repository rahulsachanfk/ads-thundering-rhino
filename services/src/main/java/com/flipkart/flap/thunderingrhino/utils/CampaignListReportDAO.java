package com.flipkart.flap.thunderingrhino.utils;

import com.flipkart.flap.thunderingrhino.entities.CampaignDayReport;
import com.flipkart.flap.thunderingrhino.entities.CampaignListingReport;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by rahul.sachan on 25/05/16.
 */

@RegisterMapper(CampaignListingReport.CampaignListingMapper.class)
public interface CampaignListReportDAO {
    @SqlQuery("select campaign_id , banner_id,sum(views) views, sum(engagements) action,sum(converted_units) conversions,sum(cost) Ex_budget,sum(revenue) revenue, sum(engagements)/sum(views) AR, sum(converted_units)/sum(engagements) CVR,sum(`revenue`)/sum(cost) ROI from banner_daily_statistics_v2 where campaign_id=:campaign_id group by banner_id;")
    List<CampaignListingReport> campaignListingReport(@Bind("campaign_id") String campaign_id);

    @Mapper(CampaignListingReport.CampaignListingMonthlyMapper.class)
    @SqlQuery("select campaign_id, sum(views) views, sum(engagements) action,sum(converted_units) conversion,sum(cost) Ex_budget,sum(revenue) revenue, sum(engagements)/sum(views) AR, sum(converted_units)/sum(engagements) CVR,sum(`revenue`)/sum(cost) ROI from banner_daily_statistics_v2 where `interval_start`>:str_date and `interval_start`< :end_date and advertiser_id=\"3749\" group by campaign_id;")
    List<CampaignListingReport> campaignListingMonthlyReport(@Bind("str_date") String str_date,@Bind("end_date") String end_date);

    @Mapper(CampaignDayReport.CampaignDayReportMapper.class)
    @SqlQuery("select str_to_date(interval_start,'%Y%m%d') date,sum(views) views,sum(`engagements`) action,sum(cost) cost,sum(`converted_units`) converted_units,sum(`revenue`) revenue,sum(`indirectly_converted_units`) indirectly_converted_units, sum(`indirect_revenue`) indirect_revenue from `banner_daily_statistics_v2` where campaign_id=:campaign_id group by interval_start;")
    List<CampaignDayReport> campaignDayReport(@Bind("campaign_id") String campaign_id);

}
