package com.flipkart.flap.thunderingrhino.utils;

/**
 * Created by pavan.t on 16/12/15.
 */

public class ConfigNotFoundException extends Throwable {
    private String message;

    public ConfigNotFoundException(String key) {
        super(key + " key not found.");
    }
}

