package com.flipkart.flap.thunderingrhino.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Dipanjan Mukherjee (dipanjan.mukherjee@flipkart.com)
 * Date: 4/6/14
 */
public class ProductNotFoundException extends DatasourceException {
    public ProductNotFoundException(String productId, Throwable cause) {
        super("Couldn't find a product : " + productId, cause);
    }
}
