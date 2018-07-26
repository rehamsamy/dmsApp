package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseStatus {

    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<Status> list;

    public Message getMessage() {
        return message;
    }

    public List<Status> getList() {
        return list;
    }

    @Override
    public String toString() {
        return
                "ResponseAreas{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }
}