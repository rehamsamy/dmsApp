package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Reply;
import com.dmsegypt.dms.rest.model.Request;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 10/19/17.
 */

public class ResponseRelpy {

    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<Reply> list;

    public Message getMessage() {
        return message;
    }

    public List<Reply> getList() {
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
