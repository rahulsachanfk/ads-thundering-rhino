package com.flipkart.flap.thunderingrhino.utils;

import com.flipkart.flap.thunderingrhino.entities.SherlockListing;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Iterator;

/**
 * Created by pavan.t on 19/06/15.
 */

@RegisterMapper(SherlockListing.SherlockListingMapper.class)
public interface SherlockListingsDAO {

    @SqlQuery("select campaign_id,listing_id,product_id,suppressed from listings where campaign_id=:campaign_id")


    Iterator<SherlockListing> findSherlockCampaignById(@Bind("campaign_id") String campaign_id);

}
