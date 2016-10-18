package com.flipkart.flap.thunderingrhino.clients;

import com.flipkart.flap.thunderingrhino.utils.Builders;


public class ExternalAPIClientTest {
    private ExternalAPIClient client;

    public ExternalAPIClientTest() {
        this.client = new ExternalAPIClient(Builders.getJerseyClient(), "http://ads-external-api.vip.nm.flipkart.com/");

    }

}