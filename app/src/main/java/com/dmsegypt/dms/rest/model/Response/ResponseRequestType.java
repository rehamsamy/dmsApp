package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.RequestType;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseRequestType {

    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<RequestType> list;

    public Message getMessage() {
        return message;
    }

    public List<RequestType> getList() {
        return list;
    }

    @Override
    public String toString() {
        return
                "ResponseRequestType{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }
}