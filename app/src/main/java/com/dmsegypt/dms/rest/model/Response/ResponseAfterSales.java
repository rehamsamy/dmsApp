package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.AfterSalesItem;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseAfterSales {
    @SerializedName("List")
    @Expose
    ArrayList<AfterSalesItem> list;
    @SerializedName("Message")
            @Expose
    Message message;

    public ArrayList<AfterSalesItem> getList() {
        return list;
    }

    public void setList(ArrayList<AfterSalesItem> list) {
        this.list = list;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
