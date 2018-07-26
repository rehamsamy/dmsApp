package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Lookup;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 5/29/2017.
 */

public class ResponseGeneralLookup {

    @SerializedName("List")
    @Expose
    private java.util.List<Lookup> list;
    @SerializedName("Message")
    @Expose
    private Message message;

    public java.util.List<Lookup> getList() {
        return list;
    }

    public void setList(java.util.List<Lookup> list) {
        this.list = list;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
