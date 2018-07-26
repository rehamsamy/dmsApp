package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.DoctorSpicific;
import com.dmsegypt.dms.rest.model.Item;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 10/16/17.
 */

public class ResponseItem {
    @SerializedName("Message")
    @Expose
    private Message message;

    @SerializedName("List")
    @Expose
    private List<Item> list;
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return
                "ResponseProviderTypes{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }

}
