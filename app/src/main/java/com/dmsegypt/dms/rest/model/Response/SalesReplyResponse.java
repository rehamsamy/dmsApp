package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by amr on 29/03/2018.
 */

public class SalesReplyResponse {

    @SerializedName("List")
    @Expose
    private ArrayList<SalesReply> list = null;
    @SerializedName("Message")
    @Expose
    private Message message;

    public ArrayList<SalesReply> getList() {
        return list;
    }

    public void setList(ArrayList<SalesReply> list) {
        this.list = list;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
