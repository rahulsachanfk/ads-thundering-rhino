package com.flipkart.flap.thunderingrhino.utils;

import com.flipkart.flap.thunderingrhino.entities.CMOutboundMessage;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

/**
 * Created by pushya.gupta on 30/05/16.
 */
public interface CMOutboundMessageDAO {

    @RegisterMapper(CMOutboundMessage.CMOutboundMessageMapper.class)
    @SqlQuery("select message from outbound_messages where message like concat('%',:cmp_id,'%') ORDER BY relayed_at DESC limit 1 ")
    String getMessage(@Bind("cmp_id") String cmp_id);


}
