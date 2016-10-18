package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Dipanjan Mukherjee (dipanjan.mukherjee@flipkart.com)
 * Date: 4/6/14
 */
@Data
public class Product {
    @JsonProperty("title")
    private String title;

    @JsonProperty("sellingPrice")
    private BigDecimal price;

    @JsonProperty("fsp")
    private BigDecimal fsp;

    @JsonProperty("santaPrice")
    private BigDecimal santaPrice;

    @JsonProperty("listingPrice")
    private BigDecimal mrp;

    @JsonIgnore
    private Image image;

    @JsonProperty("images")
    private Image[] images;

    @JsonProperty("seller")
    private String seller;

    @JsonIgnore
    private URL url;

    @JsonIgnore
    private String vertical;

    @JsonProperty("productId")
    private String referenceId; // Could be product id, listing id or anything

    @JsonIgnore
    private boolean inStock;

    @JsonIgnore
    private String productState;

    @JsonIgnore
    private String listingState;

    @JsonIgnore
    boolean isDiscoverable;

    @JsonIgnore
    boolean isLive;

    @JsonProperty("showMrp")
    private boolean showMrp;

    @JsonIgnore
    private String listingStatus;

    public Product(String title, BigDecimal price, BigDecimal fsp, BigDecimal santaPrice, BigDecimal mrp, Image image, String seller, URL url,
                   String vertical, String referenceId, boolean inStock, String listingState, boolean showMrp, String productState, boolean isDiscoverable, boolean isLive) {
        this.title = title;
        this.price = price;
        this.fsp = fsp;
        if(santaPrice == null)
            this.santaPrice = BigDecimal.ZERO;
        else
            this.santaPrice = santaPrice;
        this.mrp = mrp;
        this.image = image;
        this.seller = seller;
        this.url = url;
        this.vertical = vertical;
        this.referenceId = referenceId;
        this.inStock = inStock;
        this.showMrp = showMrp;
        this.productState = productState;
        this.listingState = listingState;
        this.isDiscoverable = isDiscoverable;
        this.isLive = isLive;
    }

    public Product(String title, BigDecimal price, BigDecimal fsp, BigDecimal santaPrice, BigDecimal mrp, String seller, URL url,
                   String vertical, String referenceId, boolean inStock, String listingState, boolean showMrp, String productState, boolean isDiscoverable, boolean isLive, String listingStatus) {
        this(title, price, fsp, santaPrice, mrp, seller, url, vertical, referenceId, inStock, listingState, showMrp, productState, isDiscoverable, isLive);
        this.listingStatus = listingStatus;
    }
    public Product(String title, BigDecimal price, BigDecimal fsp, BigDecimal santaPrice, BigDecimal mrp, String seller, URL url,
                   String vertical, String referenceId, boolean inStock, String listingState, boolean showMrp, String productState, boolean isDiscoverable, boolean isLive) {
        this.title = title;
        this.price = price;
        this.fsp = fsp;
        if(santaPrice == null)
            this.santaPrice = BigDecimal.ZERO;
        else
            this.santaPrice = santaPrice;
        this.mrp = mrp;
        this.seller = seller;
        this.url = url;
        this.vertical = vertical;
        this.referenceId = referenceId;
        this.inStock = inStock;
        this.showMrp = showMrp;
        this.productState = productState;
        this.listingState = listingState;
        this.isDiscoverable = isDiscoverable;
        this.isLive = isLive;
    }

    public boolean isInStock() {
        return inStock;
    }
    public boolean isAvailable() {
        if (!isInStock() && (listingState == null || listingState.compareToIgnoreCase("comingsoon") != 0)) {
            return false;
        }
        if (!isLive) {
            return false;
        }
        if (!isDiscoverable) {
            return false;
        }
        if (productState != null && productState.compareToIgnoreCase("ready") != 0) {
            return false;
        }
        return true;
    }

    public BigDecimal getMrp() {
        return mrp;
    }

    public URL getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getFSP() {
        return fsp;
    }

    public BigDecimal getSantaPrice() {
        return santaPrice;
    }

    public Image getImage() {
        return image;
    }

    public String getSeller() {
        return seller;
    }

    public String getVertical() {
        return vertical;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public boolean isShowMRP() {
        return showMrp;
    }

    @JsonProperty("discountPercentage")
    public int getDiscountPercentage() {
        if (this.getPrice() == null || this.getMrp() == null || this.getMrp().doubleValue() == 0) {
            return 0;
        }

        return this.getMrp().subtract(this.getPrice())
                .multiply(BigDecimal.valueOf(100l))
                .divide(this.getMrp(), RoundingMode.FLOOR).intValue();
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
