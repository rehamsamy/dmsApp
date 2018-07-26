package com.dmsegypt.dms.rest.model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 6/17/2017.
 */

public class Status {
    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("Number")
    @Expose
    private String number;

    @SerializedName("Status")
    @Expose
    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
