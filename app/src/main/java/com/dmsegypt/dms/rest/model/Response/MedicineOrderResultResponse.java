package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.MedicineOrder;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud on 4/29/2018.
 */

public class MedicineOrderResultResponse {

    @SerializedName("List")
    @Expose
    private List<MedicineOrder> list = null;
    @SerializedName("Message")
    @Expose
    private Message message;

    public List<MedicineOrder> getList() {
        return list;
    }

    public void setList(List<MedicineOrder> list) {
        this.list = list;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
