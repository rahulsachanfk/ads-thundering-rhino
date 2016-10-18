package com.flipkart.flap.thunderingrhino.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jagmeet.singh on 03/01/15.
 */
public class HtmlUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlUtils.class);

    public static String getGetResponse(String urlString) {
        String responseString = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            responseString = response.toString();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("'GET' request to URL : " + urlString);
                LOGGER.debug("Response Code : " + responseCode);
                LOGGER.debug("Response returned: " + responseString);
            }

        } catch (MalformedURLException e) {
           LOGGER.error("Unable to parse URL string: " + urlString, e);
        } catch (IOException e) {
            LOGGER.error("Unable to retrieve response from URL: " + urlString, e);
        }

        return responseString;
    }
}
