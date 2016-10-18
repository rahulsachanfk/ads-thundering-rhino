package com.flipkart.flap.thunderingrhino.utils;

import com.flipkart.flap.thunderingrhino.entities.MadisonMessage;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

/**
 * Created by pushya.gupta on 07/06/16.
 */
public interface MadisonMessageDAO {

    @RegisterMapper(MadisonMessage.MadisonMessageMapper.class)
    @SqlQuery("select message, custom_headers from outbound_messages where message like concat('%',:cmp_id,'%') ORDER BY relayed_at DESC limit 1")
    MadisonMessage getMessage(@Bind("cmp_id") String cmp_id );
}
