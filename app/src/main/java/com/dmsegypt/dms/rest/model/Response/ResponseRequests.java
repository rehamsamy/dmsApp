package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Request;
import com.dmsegypt.dms.rest.model.RequestType;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 24-Sep-17.
 */

public class ResponseRequests {
    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<Request> list;

    public Message getMessage() {
        return message;
    }

    public List<Request> getList() {
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
