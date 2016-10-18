package com.flipkart.flap.thunderingrhino.utils;

/**
 * Created by suchi.sethi on 21/08/15.
 */


import com.flipkart.flap.thunderingrhino.entities.ApplicationDetail;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by suchi.sethi on 01/12/15.
 */

@RegisterMapper(ApplicationDetail.ApplicationDetailMapper.class)
public interface ApplicationDAO {
    @SqlQuery("select app_id, instance_grp, port, url from app_id_details where app_id = :app_id")
    List<ApplicationDetail> applicationEntry(@Bind("app_id") String app_id);

    @Mapper(ApplicationDetail.appidListMapper.class)
    @SqlQuery("select product, app_id from app_id_details group by app_id order by product")
    List<ApplicationDetail> getappidList();
}
