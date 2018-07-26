package com.dmsegypt.dms.rest.model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 6/9/2017.
 */

public class StatusResponse {

    @SerializedName("Code")
    @Expose
    public Integer code;
    @SerializedName("Details")
    @Expose
    public String details;

    @SerializedName("Userid")
    @Expose
    public String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
