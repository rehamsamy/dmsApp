package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Pharmacy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud on 4/19/2018.
 */

public class GetPharmaciesReponse {

    @SerializedName("List")
    @Expose
    private List<Pharmacy> list = null;
    @SerializedName("Message")
    @Expose
    private Message message;

    public List<Pharmacy> getList() {
        return list;
    }

    public void setList(List<Pharmacy>list) {
        this.list = list;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
