package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.UserPhone;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUserPhones {

    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<UserPhone> list;

    public Message getMessage() {
        return message;
    }

    public List<UserPhone> getList() {
        return list;
    }

    @Override
    public String toString() {
        return
                "ResponseUserPhone{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }
}