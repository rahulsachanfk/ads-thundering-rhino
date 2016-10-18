package com.flipkart.flap.thunderingrhino.configuration;

import lombok.Data;

/**
 * Created by dipanjan.mukherjee on 17/02/15.
 *
 * Defines an enable/disable feature flag.
 */
@Data
public class Flipper {
    private boolean enabled = false;
}
