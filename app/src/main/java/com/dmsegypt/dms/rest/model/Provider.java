package com.dmsegypt.dms.rest.model;

import android.text.TextUtils;

import com.dmsegypt.dms.utils.DataUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Provider implements Serializable {

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("Address")
    @Expose
    private String address;

    @SerializedName("Degree")
    @Expose
    private String degree;

    @SerializedName("AreaCode")
    @Expose
    private String areaCode;

    @SerializedName("Tel")
    @Expose
    private List<String> tel;

    @SerializedName("Id")
    @Expose
    private String id;

    @SerializedName("ProviderType")
    @Expose
    private String providerType;

    @SerializedName("Name")
    @Expose
    private String name;

    public String getEmail() {
        if (email == null) return "";
        return email;
    }

    public String getAddress() {
        if (address == null) return "";
        return address;
    }

    public String getDegree() {
        return degree;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getTel() {
        if (tel == null) return "";
        return TextUtils.join("-", tel);
    }

    public String getId() {
        return id;
    }

    public String getProviderType() {
        return providerType;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return
                "Provider{" +
                        "email = '" + email + '\'' +
                        ",address = '" + address + '\'' +
                        ",degree = '" + degree + '\'' +
                        ",areaCode = '" + areaCode + '\'' +
                        ",tel = '" + tel + '\'' +
                        ",id = '" + id + '\'' +
                        ",providerType = '" + providerType + '\'' +
                        ",name = '" + name + '\'' +
                        "}";
    }

    public String detailsToShare() {
        return
                name + "\n" +
                        "Address : " + address + "\n" +
                        "Phone number : " + tel + "\n" +
                        "Email : " + email + "\n" +
                        "Type : " + DataUtils.getTypeNameById(providerType);
    }
}