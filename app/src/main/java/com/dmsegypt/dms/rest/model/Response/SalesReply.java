package com.dmsegypt.dms.rest.model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amr on 29/03/2018.
 */

public class SalesReply {
    @SerializedName("code")
            @Expose
    String code;
    @SerializedName("Name")
            @Expose
    String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
