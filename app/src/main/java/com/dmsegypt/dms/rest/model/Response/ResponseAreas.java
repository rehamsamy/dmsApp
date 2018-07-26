package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseAreas {

    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<AreaItem> list;

    public Message getMessage() {
        return message;
    }

    public List<AreaItem> getList() {
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