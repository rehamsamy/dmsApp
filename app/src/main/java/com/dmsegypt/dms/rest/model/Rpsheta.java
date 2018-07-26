package com.dmsegypt.dms.rest.model;

/**
 * Created by Mahmoud on 4/22/2018.
 */

public class Rpsheta {

    String imageUrl;
    String orderDate;
    String name;
    String state;

    public Rpsheta(String imageUrl, String orderDate, String name, String state) {
        this.imageUrl = imageUrl;
        this.orderDate = orderDate;
        this.name = name;
        this.state = state;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
