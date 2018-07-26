package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 6/09/17.
 */

public class UserPhone {

    @SerializedName("USER_ID")
    private String user_id;

    @SerializedName("USER_NAME")
    private String user_name;

    @SerializedName("USER_PWD")
    private String user_pwd;

    @SerializedName("CITE_EMP_CODE")
    private String CITE_EMP_CODE;


    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public String getCITE_EMP_CODE() {
        return CITE_EMP_CODE;
    }
}
