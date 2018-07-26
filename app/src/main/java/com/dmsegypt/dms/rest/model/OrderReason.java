package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 25/12/17.
 */

public class OrderReason {
    @SerializedName("Id")
    private String id;

    @SerializedName("Reason_aname")
    private String reason_aname;

    @SerializedName("Reason_ename")
    private String reason_ename;

    public OrderReason(String id, String reason_aname, String reason_ename) {
        this.id = id;
        this.reason_aname = reason_aname;
        this.reason_ename = reason_ename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason_aname() {
        return reason_aname;
    }

    public void setReason_aname(String reason_aname) {
        this.reason_aname = reason_aname;
    }

    public String getReason_ename() {
        return reason_ename;
    }

    public void setReason_ename(String reason_ename) {
        this.reason_ename = reason_ename;
    }
}
