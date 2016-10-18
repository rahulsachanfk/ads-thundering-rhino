package com.flipkart.flap.thunderingrhino.utils;

import com.flipkart.flap.thunderingrhino.entities.CampaignInfo;
import com.flipkart.flap.thunderingrhino.entities.ProductDetail;
import com.flipkart.flap.thunderingrhino.entities.RelevanceTagging;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by suchi.sethi on 11/01/16.
 */


public interface RelevanceTaggingDAO {

    @RegisterMapper(RelevanceTagging.RelevanceTaggingMapper.class)
    @SqlQuery("select id from adserverdb.sellers where seller_id = :seller_id")
    int getAdIDbySellerID (@Bind("seller_id") String seller_id);

    @RegisterMapper(CampaignInfo.CampaignInfoMapper.class)
    @SqlQuery("select campaign_id, name, status from campaigns where advertiser_id = :advertiser_id and status ='ready'")
    List<CampaignInfo> getCampaignbyAdID(@Bind("advertiser_id") String advertiser_id);

    @RegisterMapper(ProductDetail.ProductDetailMapper.class)
    @SqlQuery("select product_id from listings where campaign_id = :campaign_id")
    List<ProductDetail> getProductIDbyCampaign(@Bind("campaign_id") String campaign_id);


}
