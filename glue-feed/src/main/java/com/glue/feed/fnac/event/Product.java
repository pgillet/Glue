package com.glue.feed.fnac.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

    String name;
    String program;
    String number;
    String description;
    String longDescription;
    String manufacturer;
    String price;
    String terms;
    String shippingHandling;
    String shippingHandlingCost;
    String lastModified;
    String validFrom;
    String validTo;
    String smallImage;
    String mediumImage;
    String largeImage;
    String deliveryTime;
    String currencyCode;
    String extra1;
    String extra2;
    String extra3;
    String merchantCategory;
    String deepLink;
    String basePriceText;

    @Override
    public String toString() {
	return "Product [name=" + name + ", program=" + program + ", number="
		+ number + ", description=" + description
		+ ", longDescription=" + longDescription + ", manufacturer="
		+ manufacturer + ", price=" + price + ", terms=" + terms
		+ ", shippingHandling=" + shippingHandling
		+ ", shippingHandlingCost=" + shippingHandlingCost
		+ ", lastModified=" + lastModified + ", validFrom=" + validFrom
		+ ", validTo=" + validTo + ", smallImage=" + smallImage
		+ ", mediumImage=" + mediumImage + ", largeImage=" + largeImage
		+ ", deliveryTime=" + deliveryTime + ", currencyCode="
		+ currencyCode + ", extra1=" + extra1 + ", extra2=" + extra2
		+ ", extra3=" + extra3 + ", merchantCategory="
		+ merchantCategory + ", deepLink=" + deepLink
		+ ", basePriceText=" + basePriceText + "]";
    }

};
