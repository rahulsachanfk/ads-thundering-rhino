package com.flipkart.flap.thunderingrhino.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Dipanjan Mukherjee (dipanjan.mukherjee@flipkart.com)
 * Date: 4/6/14
 */
public class Image {

    @JsonIgnore
    private int height;

    @JsonIgnore
    private int width;

    @JsonProperty("url")
    private URL url;


    public Image( URL url,  int height, int width) {
        this.height = height;
        this.width = width;
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public URL getUrl() {
        return url;
    }

    @JsonProperty("size")
    public String getSize() {
        return String.format("%sx%s", this.width, this.height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;

        Image image = (Image) o;

        if (height != image.height) return false;
        if (width != image.width) return false;
        if (!url.equals(image.url)) return false;

        return true;
    }
}

