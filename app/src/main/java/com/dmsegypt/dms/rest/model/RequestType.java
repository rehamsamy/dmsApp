package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

public class RequestType {
    public RequestType(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public  RequestType(){}

    @SerializedName("Id")
    private String id;

    @SerializedName("Name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
//        return
//                "AreaItem{" +
//                        "id = '" + id + '\'' +
//                        ",name = '" + name + '\'' +
//                        "}";
    }
}