package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 9/10/2017.
 */

public class DoctorSpicific {
    @SerializedName("Id")
    @Expose
    private String id;

    @SerializedName("Name")
    @Expose
    private String name;

    public DoctorSpicific(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return getName();
//		return
//			"ProviderType{" +
//			"id = '" + id + '\'' +
//			",name = '" + name + '\'' +
//			"}";
    }
}
