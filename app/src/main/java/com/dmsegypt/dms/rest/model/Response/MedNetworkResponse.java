package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.MedNetworkItem;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud on 4/12/2018.
 */

public class MedNetworkResponse
{


    @SerializedName("List")
    @Expose
    private List<MedNetworkItem> list = null;
    @SerializedName("Message")
    @Expose
    private Message message;

    public List<MedNetworkItem>  getList() {
        return list;
    }

    public void setList(List<MedNetworkItem>  list) {
        this.list = list;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
