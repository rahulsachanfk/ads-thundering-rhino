package com.flipkart.flap.thunderingrhino.utils;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.flipkart.flap.thunderingrhino.clients.ExternalAPIClient;
import com.flipkart.flap.thunderingrhino.configuration.ServicesConfiguration;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * Created by dipanjan.mukherjee on 17/02/15.
 *
 * Various static methods to get Objects from dropwizard-configuration
 * instances.
 */
public class Builders {
    public static ExternalAPIClient getExternalAPIClient(ServicesConfiguration configuration, Client client) {
        return new ExternalAPIClient(client, configuration.getExternalAPIBaseURL());
    }

    public static Client getJerseyClient() {
        ClientConfig clientConfig = new DefaultClientConfig(JacksonJaxbJsonProvider.class);
        return Client.create(clientConfig);
    }
}
