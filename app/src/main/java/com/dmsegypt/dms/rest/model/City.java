package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

public class City {
    public City(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public City() {
    }

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
//                "City{" +
//                        "id = '" + id + '\'' +
//                        ",name = '" + name + '\'' +
//                        "}";
    }
}