package com.flipkart.flap.thunderingrhino.utils;

import com.flipkart.flap.thunderingrhino.entities.CampaignAudit;
import com.flipkart.flap.thunderingrhino.entities.CampaignDetails;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by pavan.t on 22/06/15.
 */



@RegisterMapper(CampaignAudit.CampaignAuditMapper.class)

public interface CampaignAuditDAO {

    @SqlQuery("select * from `campaigns_audit` where campaign_id =:campaign_id order by `timestamp_action`")
    List<CampaignAudit> findCampaignAuditById(@Bind("campaign_id") String campaign_id);

    @Mapper(CampaignDetails.CampaignDetailsMapper.class)
    @SqlQuery("select campaign_id,name,start_date,end_date,status from campaigns where `advertiser_id`='3749';")
    List<CampaignDetails> campaignDetailsList();



}
